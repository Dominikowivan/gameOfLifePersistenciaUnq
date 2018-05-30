package edu.unq.pconc.gameoflife.solution;

import edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo.EstrategiaDeAgregarVecinos;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo.EstrategiaDeConsumo;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo.EstrategiaDeEliminarALosMuertos;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo.EstrategiaDeLimpiarCeldas;
import edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo.EstrategiaDeNuevasCeldas;

public class Consumidor extends Worker {

	EstrategiaDeConsumo estrategiaDeConsumo;
	
	public Consumidor(BufferDeRegiones bufferDeRegiones, CabinaDeDescanso cabinaDeDescanso, MonitorDeQueTerminaronLosTrabajadores monitorTrabajador) {
		super(bufferDeRegiones, cabinaDeDescanso,monitorTrabajador);
	}

	@Override
	public void run() {
		try {
			
			
			estrategiaDeConsumo = new EstrategiaDeLimpiarCeldas();
			unBufferDeRegiones.consumirRegion(this);
			unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,1);
			
			estrategiaDeConsumo = new EstrategiaDeAgregarVecinos();
			unBufferDeRegiones.consumirRegion(this);
			unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,2);
			
			estrategiaDeConsumo = new EstrategiaDeEliminarALosMuertos();
			unBufferDeRegiones.consumirRegion(this);
			unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,3);
        	
			estrategiaDeConsumo = new EstrategiaDeNuevasCeldas();
			unBufferDeRegiones.consumirRegion(this);
        	monitorTrabajador.termine(this);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void consumir(RegionDeTablero regionDeTablero, GameOfLifeGrid golg) {
		estrategiaDeConsumo.consumir(regionDeTablero,golg);  
	}


}
