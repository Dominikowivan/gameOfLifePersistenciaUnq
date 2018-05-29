package edu.unq.pconc.gameoflife.solution;

import java.util.*;

public class BufferDeRegiones {

    private GameOfLifeGrid golg;


    private Integer cantidadDeCelulasATrabajar;

    private Integer cantidadDeRegiones;

    private Integer cantidadDeTrabajadores;
    private Integer cantidadDeTrabajadoresDescansando;


    private Integer etapa;
    private Integer etapaProxima;


    private Stack<RegionDeTablero> buffer;
    private Integer tamanioDelBuffer;
    




    public BufferDeRegiones(int elTamanioDelBuffer, int unaCantidadDeRegiones ) {
        cantidadDeTrabajadores            = 2;
        cantidadDeTrabajadoresDescansando =0;

    	cantidadDeRegiones           = unaCantidadDeRegiones;

        tamanioDelBuffer              = elTamanioDelBuffer;
        buffer                       = new Stack<RegionDeTablero>();
}		


	public synchronized void producir(Worker unWorker) throws InterruptedException {
        while (this.faltanTrabajarRegiones()){
            if(!this.hayLugarEnElBuffer()){
                wait();
            }

            Integer celulasParaTrabajar = this.tomarCelulasParaTrabajar();
            RegionDeTablero regionDeTablero = unWorker.construirRegion(celulasParaTrabajar);

            buffer.push(regionDeTablero);
            notifyAll();
        }


        System.out.println("Soy un productor, termine, y Llegue al descanso de la etapa " + this.etapa + "por que faltan trabajar regiones es "+ this.faltanTrabajarRegiones());
        this.proximaEtapa();
        System.out.println("Soy un productor, y Salgo a trabajar en la etapa "+this.etapa);
        if (etapa != 5){
            this.producir(unWorker);
        }
    }

    public synchronized void resetearCeldas(Worker unWorker) throws InterruptedException {
        while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }

            RegionDeTablero regionDeTablero = buffer.pop();

            unWorker.resetearRegion(regionDeTablero);
            notifyAll();
        }
        System.out.println("Soy un consumidor, termine, y Llegue al descanso de la etapa " + this.etapa);
        this.proximaEtapa();
        System.out.println("Soy un consumidor, y Salgo a trabajar en la etapa "+this.etapa);
        this.agregarVecinos(unWorker);

    }

    private void proximaEtapa() throws InterruptedException {
        cantidadDeTrabajadoresDescansando++;
        Integer proximaEtapa = this.etapaProxima;

        if ( cantidadDeTrabajadoresDescansando ==  cantidadDeTrabajadores){
            this.etapa= this.etapaProxima;
            etapaProxima++;
            this.resetearTablero();

            System.out.println("Se reseteo el buffer");
            System.out.println("la cantidad de trabajadores descansando son"+this.cantidadDeTrabajadoresDescansando);
            System.out.println("la cantidad de celulas a trabajaron son"+this.cantidadDeCelulasATrabajar);
            cantidadDeTrabajadoresDescansando = 0;

            notifyAll();
        }

        while(this.etapa != proximaEtapa) {
            wait(); }


    }





    private void resetearTablero() {
        if (etapa ==4){
            golg.setEnumNext();
            System.out.println("Mi cantidad de celulas a trabajar es antes: " + cantidadDeCelulasATrabajar);
            this.cantidadDeCelulasATrabajar = golg.getNextShape().size();
            System.out.println("Mi cantidad de celulas a trabajar es despues: " + cantidadDeCelulasATrabajar);


        }
        else {
            golg.setEnumCurrent();
            System.out.println("Mi cantidad de celulas a trabajar es antes: " + cantidadDeCelulasATrabajar);
            this.cantidadDeCelulasATrabajar = golg.getCurrentShape().size();
            System.out.println("Mi cantidad de celulas a trabajar es despues: " + cantidadDeCelulasATrabajar);
        }



    }

    private boolean hayRegionEnBuffer() {
        return !buffer.empty();
    }

    private boolean faltanConsumirRegiones() {
        return cantidadDeCelulasATrabajar > 0 || this.hayRegionEnBuffer();
    }

    private Integer tomarCelulasParaTrabajar() {
        Integer celdasParaTrabajar = 0;
        if (cantidadDeCelulasATrabajar > cantidadDeRegiones){
            cantidadDeCelulasATrabajar = cantidadDeCelulasATrabajar- cantidadDeRegiones;
            celdasParaTrabajar = cantidadDeRegiones;
        }
        else{
            celdasParaTrabajar = cantidadDeCelulasATrabajar;
            cantidadDeCelulasATrabajar = 0;
        }
        return celdasParaTrabajar;
    }


    private boolean hayLugarEnElBuffer() {
        return buffer.size() < tamanioDelBuffer;
    }

    private boolean faltanTrabajarRegiones() {
        return cantidadDeCelulasATrabajar > 0;
    }




	public void agregarVecinos(Worker unWorker) throws InterruptedException {
        while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }

            RegionDeTablero regionDeTablero = buffer.pop();

            unWorker.agregarVecinosRegion(regionDeTablero);

            notifyAll();
        }
        System.out.println("Soy un consumidor, termine, y Llegue al descanso de la etapa " + this.etapa);
        this.proximaEtapa();
        System.out.println("Soy un consumidor, y Salgo a trabajar en la etapa "+this.etapa);
        this.eliminarMuertos(unWorker);
	}


	public void eliminarMuertos(Worker unWorker) throws InterruptedException {
        while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }

            RegionDeTablero regionDeTablero = buffer.pop();

            unWorker.eliminarMuertosRegion(regionDeTablero);

            notifyAll();
        }
        System.out.println("Soy un consumidor, termine, y Llegue al descanso de la etapa " + this.etapa);
        this.proximaEtapa();
        System.out.println("Soy un consumidor, y Salgo a trabajar en la etapa "+this.etapa);
        this.nuevasCeldas(unWorker);
	}


	public void nuevasCeldas(Worker unWorker) throws InterruptedException {
        while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }

            RegionDeTablero regionDeTablero = buffer.pop();

            unWorker.nuevasCeldas(regionDeTablero);

            notifyAll();
        }
        System.out.println("Soy un consumidor, termine, y Llegue al descanso de la etapa " + this.etapa);
        this.proximaEtapa();
        System.out.println("Soy un consumidor, y Salgo a trabajar en la etapa "+this.etapa);
	}

	public void cambiarCelulasParaTrabajar(int cantidad) {
		this.cantidadDeCelulasATrabajar = cantidad;
	}
    public void cambiarCantidaddeTrabajadores(int cantidad) {
        this.cantidadDeTrabajadores = cantidad;
    }

    public void resetEtapas() {
        this.etapa= 1;
        this.etapaProxima= 2;
    }

    public void setGolg(GameOfLifeGrid gameOfLifeGrid) {
        golg = gameOfLifeGrid;
    }
}
