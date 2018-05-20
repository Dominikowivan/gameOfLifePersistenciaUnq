package edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo;


import edu.unq.pconc.gameoflife.solution.BufferDeRegiones;
import edu.unq.pconc.gameoflife.solution.Worker;

public class EstrategiaDeProduccion implements EstrategiaDeTrabajo {

    public EstrategiaDeProduccion() {

    }

    @Override
    public void trabajar(BufferDeRegiones unBufferDeRegiones, Worker unWorker) throws InterruptedException {
        unBufferDeRegiones.producir(unWorker);
    }
}
