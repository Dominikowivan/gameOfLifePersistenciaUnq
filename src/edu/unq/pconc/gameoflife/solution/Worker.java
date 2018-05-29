package edu.unq.pconc.gameoflife.solution;


import edu.unq.pconc.gameoflife.solution.EstrategiasDeTrabajo.EstrategiaDeTrabajo;

import java.util.Enumeration;

public class Worker extends Thread {

    private BufferDeRegiones unBufferDeRegiones;
    private EstrategiaDeTrabajo unaEstrategiaDeTrabajo;
    private GameOfLifeGrid golg;

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

    public void resetearRegion(RegionDeTablero regionDeTablero) {

        golg.resetearRegion(regionDeTablero);


    }

	public void agregarVecinosRegion(RegionDeTablero regionDeTablero) {
	        golg.agregarVecinosRegion(regionDeTablero);
	}

	public void setGolg(GameOfLifeGrid golg) {
		this.golg = golg;
	}

	public void eliminarMuertosRegion( RegionDeTablero regionDeTablero) {
	    golg.eliminarMuertosRegion(regionDeTablero);
	}

	public void nuevasCeldas(RegionDeTablero regionDeTablero) {
		golg.nuevasCeldas(regionDeTablero);
	}
}
