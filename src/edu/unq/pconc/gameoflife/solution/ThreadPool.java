package edu.unq.pconc.gameoflife.solution;

import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeProduccion;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeConsumo;

import java.util.Enumeration;
import java.util.Hashtable;

public class ThreadPool {

    // Los workers se van a dividir en 2, unos productores, y otros consumidores.
    // Los productores son los que van a ir construyendo las regiones del tablero y dejandolas en el buffer.
    // los consumidores son los que las van a ir permutando las regiones en el buffer.



    private Integer cantidadDeWorkersProductores;
    private Integer cantidadDeWorkersConsumidores;

    private BufferDeRegiones bufferDeRegiones;

    public ThreadPool(BufferDeRegiones unBuffer){
        // por default hay un solo Thread, que trabaja como productor.
        cantidadDeWorkersProductores = 1;
        cantidadDeWorkersConsumidores = 1;

        bufferDeRegiones = unBuffer;
    }


    public void cambiarCantidadDeWorkers(int threads) {
        //  No se puede trabajar con menos de 2 Thread, asi que cualquier numero menor a este se ignora.
        if (threads <= 2 ){
            cantidadDeWorkersProductores = 1;
            cantidadDeWorkersConsumidores = 1;
            bufferDeRegiones.cambiarCantidaddeTrabajadores(2);
        }
        else{
            // Se setean la mitad como productores, y la mitad como consumidores.

            cantidadDeWorkersProductores = threads/2+ threads%2;
            cantidadDeWorkersConsumidores =  threads/2 ;


            bufferDeRegiones.cambiarCantidaddeTrabajadores(threads);

        }
    }


    public void ponerProductoresATrabajar() {
        //por cada uno de mis productores
        for (int i = 0; i < cantidadDeWorkersProductores ; i++) {

            //creo el worker que va a trabajar produciendo, y lo pongo a trabajar.
            Worker worker = new Worker(this.bufferDeRegiones, new EstrategiaDeProduccion());
            worker.start();
        }
    }

    
    public void ponerConsumidoresATrabajar(GameOfLifeGrid golg) {
        //por cada uno de misz consumidores
        for (int i = 0; i < cantidadDeWorkersConsumidores ; i++) {

            //creo el worker que va a trabajar consumiento, y lo pongo a trabajar.
            Worker worker = new Worker(this.bufferDeRegiones, new EstrategiaDeConsumo());
            worker.setGolg(golg);
            worker.start();
        }
    }


	public void celulasATrabajar(int cantidad) {
		bufferDeRegiones.cambiarCelulasParaTrabajar(cantidad);
	}


    public void gameOfLifeGrid(GameOfLifeGrid gameOfLifeGrid) {
        bufferDeRegiones.setGolg(gameOfLifeGrid);
    }
}
