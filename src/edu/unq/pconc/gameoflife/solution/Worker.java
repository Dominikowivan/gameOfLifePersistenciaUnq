package edu.unq.pconc.gameoflife.solution;


import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeTrabajo;

import java.util.Enumeration;

public class Worker extends Thread {

    private BufferDeRegiones unBufferDeRegiones;
    private EstrategiaDeTrabajo unaEstrategiaDeTrabajo;

    public Worker(BufferDeRegiones bufferDeRegiones, EstrategiaDeTrabajo estrategiaDeTrabajo) {
        unBufferDeRegiones     = bufferDeRegiones;
        unaEstrategiaDeTrabajo = estrategiaDeTrabajo;
    }

    @Override
    public void run() {
        try {
           unaEstrategiaDeTrabajo.trabajar(unBufferDeRegiones, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public RegionDeTablero construirRegion(Integer celdasParaTrabajar) {

        return new RegionDeTablero(celdasParaTrabajar);

    }

    public void resetearRegion(Enumeration e, RegionDeTablero regionDeTablero) {

        for (int i = 0; i < regionDeTablero.cantidadDeCeldas ; i++) {
            Cell cell = (Cell) e.nextElement();
            cell.neighbour = 0;
        }
    }
}
