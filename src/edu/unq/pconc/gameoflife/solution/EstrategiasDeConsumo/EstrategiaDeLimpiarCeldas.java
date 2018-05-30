package edu.unq.pconc.gameoflife.solution.EstrategiasDeConsumo;

import edu.unq.pconc.gameoflife.solution.Cell;
import edu.unq.pconc.gameoflife.solution.GameOfLifeGrid;
import edu.unq.pconc.gameoflife.solution.RegionDeTablero;

public class EstrategiaDeLimpiarCeldas implements EstrategiaDeConsumo {

	@Override
	public void consumir(RegionDeTablero region, GameOfLifeGrid golg) {
		Cell cell = region.getCell();
		golg.resetearCelula(cell);
	}

}
