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

	public void resetearRegion(RegionDeTablero regionDeTablero) {
		Cell cell = regionDeTablero.getCell();
		cell.neighbour = 0;
	}
	
	//recordatorio de que si falla algo, seguro es esto
	public void agregarVecinosRegion(RegionDeTablero regionDeTablero, GameOfLifeGrid golg) {
		int col, row;
		Cell cell = regionDeTablero.getCell();
	    col = cell.col;
	    row = cell.row;
	      
	    golg.addNeighbour( col-1, row-1 );
	    golg.addNeighbour( col, row-1 );
	    golg.addNeighbour( col+1, row-1 );
	    golg.addNeighbour( col-1, row );
	    golg.addNeighbour( col+1, row );
	    golg.addNeighbour( col-1, row+1 );
	    golg.addNeighbour( col, row+1 );
	    golg.addNeighbour( col+1, row+1 );
	}

	public void eliminarMuertosRegion(RegionDeTablero regionDeTablero,GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell();  
		if ( cell.neighbour != 3 && cell.neighbour != 2 ) {
		        golg.currentShape.remove( cell );
		}	
	}

	public void nuevasCeldas(RegionDeTablero regionDeTablero,GameOfLifeGrid golg) {
		Cell cell = regionDeTablero.getCell(); 
		if ( cell.neighbour == 3 ) {
		        golg.setCell( cell.col, cell.row, true );
		}
		
	}


}
