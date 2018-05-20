package edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo;

import edu.unq.pconc.gameoflife.solution.BufferDeRegiones;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeTrabajo;
import edu.unq.pconc.gameoflife.solution.Worker;

import java.util.Enumeration;

public class EstrategiaDeReseteoDeCeldas implements EstrategiaDeTrabajo {
    private Enumeration anEnum;

    public EstrategiaDeReseteoDeCeldas(Enumeration e) {
        anEnum = e;
    }

    @Override
    public void trabajar(BufferDeRegiones unBufferDeRegiones, Worker unWorker) throws InterruptedException {
        unBufferDeRegiones.resetearCeldas(anEnum,unWorker);
    }
}
