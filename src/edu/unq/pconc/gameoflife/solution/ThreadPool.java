package edu.unq.pconc.gameoflife.solution;

import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeProduccion;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeReseteoDeCeldas;

import java.util.Enumeration;

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
        cantidadDeWorkersConsumidores = 0;

        bufferDeRegiones = unBuffer;
    }


    public void cambiarCantidadDeWorkers(int threads) {
        //  No se puede trabajar con menos de 1 Thread, asi que cualquier numero menor a este se ignora.
        if (threads <= 1 ){
            cantidadDeWorkersProductores = 1;
            cantidadDeWorkersConsumidores = 0;
        }
        else{
            // Se setean la mitad como productores, y la mitad como consumidores.
            cantidadDeWorkersProductores = threads/2+ threads%2;
            cantidadDeWorkersConsumidores =  threads/2 ;
        }
    }


    public void ponerProductoresATrabajar() {
        //por cada uno de mis productores
        for (int i = 0; i <= cantidadDeWorkersProductores ; i++) {

            //creo el worker que va a trabajar produciendo, y lo pongo a trabajar.
            Worker worker = new Worker(this.bufferDeRegiones, new EstrategiaDeProduccion());
            worker.start();
        }
    }

    public void ponerConsumidoresAResetearCeldas(Enumeration e) {
        //por cada uno de mis consumidores
        for (int i = 0; i <= cantidadDeWorkersConsumidores ; i++) {

            //creo el worker que va a trabajar consumiento, y lo pongo a trabajar.
            Worker worker = new Worker(this.bufferDeRegiones, new EstrategiaDeReseteoDeCeldas(e));
            worker.start();
        }
    }

}
