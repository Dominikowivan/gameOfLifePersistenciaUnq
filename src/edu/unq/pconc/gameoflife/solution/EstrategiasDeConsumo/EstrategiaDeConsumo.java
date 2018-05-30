package edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo;

import edu.unq.pconc.gameoflife.solution.GameOfLifeGrid;
import edu.unq.pconc.gameoflife.solution.RegionDeTablero;

public interface EstrategiaDeConsumo {
	public void consumir(RegionDeTablero region, GameOfLifeGrid golg);
}
