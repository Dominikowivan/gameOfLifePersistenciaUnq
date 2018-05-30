package edu.unq.pconc.gameoflife.solution;

import java.awt.Dimension;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.unq.pconc.gameoflife.CellGrid;
import edu.unq.pconc.gameoflife.shapes.Shape;

/**
 * Contains the cellgrid, the current shape and the Game Of Life algorithm that
 * changes it.
 *
 * @author Edwin Martin
 */
public class GameOfLifeGrid implements CellGrid {
	private int cellRows;
	private int cellCols;
	private int generations;
	private static Shape[] shapes;

	private ThreadPool unThreadpool;
	private BufferDeRegiones bufferParaTrabajadores;
	private CabinaDeDescanso unaCabinaDeDescanso;
	private MonitorDeQueTerminaroLosTrabajadores unMonitorTrabajadores;
	private Integer threadsWorking;
	
	
	public  Enumeration e;
	public Cell cell;
	public int col, row;
	public int neighbours;
	
	/**
	 * Contains the current, living shape. It's implemented as a hashtable. Tests
	 * showed this is 70% faster than Vector.
	 */
	Hashtable currentShape;
	Hashtable nextShape;
	/**
	 * Every cell on the grid is a Cell object. This object can become quite large.
	 */
	private Cell[][] grid;

	/**
	 * Contructs a GameOfLifeGrid.
	 * 
	 * @param cellCols
	 *            number of columns
	 * @param cellRows
	 *            number of rows
	 */
	public GameOfLifeGrid() {
		this.cellCols = 0;
		this.cellRows = 0;
		currentShape = new Hashtable();
		nextShape = new Hashtable();

		grid = new Cell[cellCols][cellRows];
		for (int c = 0; c < cellCols; c++)
			for (int r = 0; r < cellRows; r++)
				grid[c][r] = new Cell(c, r);
		bufferParaTrabajadores = new BufferDeRegiones(3, this);
		unMonitorTrabajadores = new MonitorDeQueTerminaroLosTrabajadores();
		unaCabinaDeDescanso = new CabinaDeDescanso(this);
		unThreadpool = new ThreadPool(bufferParaTrabajadores, unaCabinaDeDescanso,unMonitorTrabajadores);
		threadsWorking = 2;
	}

	/**
	 * Clears grid.
	 */
	public synchronized void clear() {
		generations = 0;
		currentShape.clear();
		nextShape.clear();
	}

	/**
	 * Create next generation of shape.
	 * @throws InterruptedException 
	 */
	public void next() {
		
		generations++;
		nextShape.clear(); 
		
		bufferParaTrabajadores = new BufferDeRegiones(3, this);
		unMonitorTrabajadores = new MonitorDeQueTerminaroLosTrabajadores();
		unaCabinaDeDescanso = new CabinaDeDescanso(this);
		unThreadpool = new ThreadPool(bufferParaTrabajadores, unaCabinaDeDescanso,unMonitorTrabajadores);
		unThreadpool.cambiarCantidadDeWorkers(threadsWorking);
		
		this.prepararLasLlavesParaEntrar();
		
		if(threadsWorking == 1){
			this.resetcells(); this.addvecinos(); this.buryDead(); this.bringNewBorns(); 	
		}
		
		else{
			unThreadpool.ponerTrabajadoresATrabajar();
			try {
				unMonitorTrabajadores.esperarAQueTerminenTrabajadores();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}

	}

	private void resetcells() {
		
		// Reset Keys
		e = currentShape.keys();

		// Parte de productor
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();

			// parte de consumidor
			resetearCelula(cell);
		}

	}
	
	public void resetearCelula(Cell aCell) {
		cell = aCell;
		System.out.println("estoy reseteando " + cell);
		cell.neighbour = 0;
	}


	private void addvecinos() {
	
		// Add neighbours
		// You can't walk through an hashtable and also add elements. Took me a couple
		// of ours to figure out. Argh!
		// That's why we have a hashNew hashtable.

		// Reset Keys
		e = currentShape.keys();

		// parte de productor
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();
			agregarVecinosACelula(cell);
		}

	}

	public void agregarVecinosACelula(Cell aCell) {
		cell = aCell;
		col = cell.col;
	    row = cell.row;
	    
	    
	    System.out.println("poniendo los vecinos de " + cell);
	    addNeighbour( col-1, row-1 );
	    addNeighbour( col, row-1 );
	    addNeighbour( col+1, row-1 );
	    addNeighbour( col-1, row );
	    addNeighbour( col+1, row );
	    addNeighbour( col-1, row+1 );
	    addNeighbour( col, row+1 );
	    addNeighbour( col+1, row+1 );
		
	}
	
	private void buryDead() {
		
		// Bury the dead
		// We are walking through an enum from we are also removing elements. Can be
		// tricky.

		// Reset keys
		e = currentShape.keys();

		// parte de productor
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();

			eliminarMuertosDeCelula(cell);
		}

	}

	public void eliminarMuertosDeCelula(Cell aCell) {
		cell = aCell;
		System.out.println("deberia matar a " + cell + " "+ (cell.neighbour != 3 && cell.neighbour != 2) );
		if ( cell.neighbour != 3 && cell.neighbour != 2 ) {
		       currentShape.remove( cell );
		}			
	}
	
	private void bringNewBorns() {

		// Bring out the new borns

		// Reset keys (usa next shape keys)
		e = nextShape.keys();

		// Parte de productor
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();

			// Parte de consumidor
			// Here is the Game Of Life rule (2):
			nuevasCeldasACelula(cell);
		}

	}
	

	public void nuevasCeldasACelula(Cell aCell) {
		cell = aCell;
		System.out.println("deberia traer a la vida a" + cell + " "+ (cell.neighbour == 3) );
		if ( cell.neighbour == 3 ) {
		        setCell( cell.col, cell.row, true );
		}
	}

	void prepararLasLlavesParaEntrar() {
	
		e = currentShape.keys();
	}

	void prepararLasLlavesParaIrse() {
		
		e = nextShape.keys();
	}

	/**
	 * Adds a new neighbour to a cell.
	 * 
	 * @param col
	 *            Cell-column
	 * @param row
	 *            Cell-row
	 */

	// si falla algo, seguro es aca.
	public synchronized void addNeighbour(int col, int row) {
		try {
			Cell cell = (Cell) nextShape.get(grid[col][row]);
			if (cell == null) {
				// Cell is not in hashtable, then add it
				Cell c = grid[col][row];
				c.neighbour = 1;
				nextShape.put(c, c);
			} else {
				// Else, increments neighbour count
				cell.neighbour++;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}
	
	
	

	/**
	 * Get value of cell.
	 * 
	 * @param col
	 *            x-coordinate of cell
	 * @param row
	 *            y-coordinate of cell
	 * @return value of cell
	 */
	public synchronized boolean getCell(int col, int row) {
		try {
			return currentShape.containsKey(grid[col][row]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
		return false;
	}

	/**
	 * Set value of cell.
	 * 
	 * @param col
	 *            x-coordinate of cell
	 * @param row
	 *            y-coordinate of cell
	 * @param c
	 *            value of cell
	 */
	public synchronized void setCell(int col, int row, boolean c) {
		try {
			Cell cell = grid[col][row];
			if (c) {
				currentShape.put(cell, cell);
			} else {
				currentShape.remove(cell);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// ignore
		}
	}

	/**
	 * Get number of generations.
	 * 
	 * @return number of generations
	 */
	public int getGenerations() {
		return generations;
	}

	/**
	 * Get dimension of grid.
	 * 
	 * @return dimension of grid
	 */
	public Dimension getDimension() {
		return new Dimension(cellCols, cellRows);
	}

	/**
	 * Resize grid. Reuse existing cells.
	 * 
	 * @see edu.unq.pconc.gameoflife.CellGrid#resize(int, int)
	 */
	public synchronized void resize(int cellColsNew, int cellRowsNew) {
		if (cellCols == cellColsNew && cellRows == cellRowsNew)
			return; // Not really a resize
		// Create a new grid, reusing existing Cell's
		Cell[][] gridNew = new Cell[cellColsNew][cellRowsNew];
		for (int c = 0; c < cellColsNew; c++)
			for (int r = 0; r < cellRowsNew; r++)
				if (c < cellCols && r < cellRows)
					gridNew[c][r] = grid[c][r];
				else
					gridNew[c][r] = new Cell(c, r);
		// Copy existing shape to center of new shape
		int colOffset = (cellColsNew - cellCols) / 2;
		int rowOffset = (cellRowsNew - cellRows) / 2;
		Cell cell;
		Enumeration e;
		nextShape.clear();
		e = currentShape.keys();
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();
			int colNew = cell.col + colOffset;
			int rowNew = cell.row + rowOffset;
			try {
				nextShape.put(gridNew[colNew][rowNew], gridNew[colNew][rowNew]);
			} catch (ArrayIndexOutOfBoundsException ex) {
				// ignore
			}
		}
		// Copy new grid and hashtable to working grid/hashtable
		grid = gridNew;
		currentShape.clear();
		e = nextShape.keys();
		while (e.hasMoreElements()) {
			cell = (Cell) e.nextElement();
			currentShape.put(cell, cell);
		}
		cellCols = cellColsNew;
		cellRows = cellRowsNew;
	}

	public void setThreads(int threads) {
		threadsWorking = threads;
	}

	
	

	

}
