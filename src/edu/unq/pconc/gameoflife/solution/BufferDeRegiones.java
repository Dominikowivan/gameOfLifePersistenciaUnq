package edu.unq.pconc.gameoflife.solution;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;

public class BufferDeRegiones {

    private Integer cantidadDeCeldasATrabajar;
    private Integer cantidadDeRegiones;

    private List<RegionDeTablero> regionesYaTrabajadas;
    private Stack<RegionDeTablero> buffer;
    private Integer tamañoDelBuffer;



    public BufferDeRegiones(Integer laCantidadDeCeldasATrabajar,Integer elTamañoDelBuffer, Integer unaCantidadDeRegiones){
        cantidadDeCeldasATrabajar    = laCantidadDeCeldasATrabajar;
        cantidadDeRegiones           = unaCantidadDeRegiones;

        tamañoDelBuffer              = elTamañoDelBuffer;
        regionesYaTrabajadas         = new ArrayList<RegionDeTablero>();
        buffer                       = new Stack<RegionDeTablero>();

    }


    public synchronized void producir(Worker unWorker) throws InterruptedException {
        while (this.faltanTrabajarRegiones()){
            if(!this.hayLugarEnElBuffer()){
                wait();
            }

            Integer celdasParaTrabajar = this.tomarCeldasParaTrabajar();
            RegionDeTablero regionDeTablero = unWorker.construirRegion(celdasParaTrabajar);

            buffer.push(regionDeTablero);
            notify();
        }

    }

    public void resetearCeldas(Enumeration e,Worker unWorker) throws InterruptedException {
        while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }

            RegionDeTablero regionDeTablero = buffer.pop();

            unWorker.resetearRegion(e,regionDeTablero);

            notify();
        }



    }

    private boolean hayRegionEnBuffer() {
        return !buffer.empty();
    }

    private boolean faltanConsumirRegiones() {
        return cantidadDeCeldasATrabajar > 0 || this.hayRegionEnBuffer();
    }

    private Integer tomarCeldasParaTrabajar() {
        Integer celdasParaTrabajar = 0;
        if (cantidadDeCeldasATrabajar > cantidadDeRegiones){
            cantidadDeCeldasATrabajar = celdasParaTrabajar - cantidadDeRegiones;
            celdasParaTrabajar = cantidadDeRegiones;
        }
        else{
            celdasParaTrabajar = cantidadDeCeldasATrabajar;
            cantidadDeCeldasATrabajar = 0;
        }
        return celdasParaTrabajar;
    }


    private boolean hayLugarEnElBuffer() {
        return buffer.size() < tamañoDelBuffer;
    }

    private boolean faltanTrabajarRegiones() {
        return cantidadDeCeldasATrabajar > 0;
    }

    public void cambiarCantidadDeCeldasParaTrabajar(Integer cantidadDeCeldasDelTablero) {
        cantidadDeCeldasATrabajar = cantidadDeCeldasDelTablero;
    }


}
