package edu.unq.pconc.gameoflife.solution;

public class RegionDeTablero {
	
	  Cell celulaAsociada;
	  
	  public RegionDeTablero(Cell unaCelula) {
		  celulaAsociada = unaCelula;
	    }

	public Cell getCell() {
		return celulaAsociada;
	}

}
