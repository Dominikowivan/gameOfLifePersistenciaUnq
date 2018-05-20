package edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo;

import edu.unq.pconc.gameoflife.solution.BufferDeRegiones;
import edu.unq.pconc.gameoflife.solution.Worker;

public interface  EstrategiaDeTrabajo {
    void trabajar(BufferDeRegiones unBufferDeRegiones, Worker unWorker) throws InterruptedException;
}
