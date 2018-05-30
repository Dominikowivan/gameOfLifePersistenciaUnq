package edu.unq.pconc.gameoflife.solution;

public class Consumidor extends Worker {

	public Consumidor(BufferDeRegiones bufferDeRegiones, CabinaDeDescanso cabinaDeDescanso) {
		super(bufferDeRegiones, cabinaDeDescanso);
	}

	@Override
	public void run() {
		try {
			unBufferDeRegiones.resetearCeldas(this);
			unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,1);
			unBufferDeRegiones.agregarVecinos(this);
			unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,2);
			unBufferDeRegiones.eliminarMuertos(this);
        	unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,3);
        	unBufferDeRegiones.nuevasCeldas(this);
        	
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void resetearRegion(RegionDeTablero regionDeTablero, GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell();
		golg.resetearCelula(cell);
		
	}
	
	//recordatorio de que si falla algo, seguro es esto
	public void agregarVecinosRegion(RegionDeTablero regionDeTablero, GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell();
		golg.agregarVecinosACelula(cell);
		
	}

	public void eliminarMuertosRegion(RegionDeTablero regionDeTablero,GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell();  
		golg.eliminarMuertosDeCelula(cell);
	
	}

	public void nuevasCeldas(RegionDeTablero regionDeTablero,GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell(); 
		golg.nuevasCeldasACelula(cell);
	}


}
