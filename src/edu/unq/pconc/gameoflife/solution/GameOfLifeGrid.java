package edu.unq.pconc.gameoflife.solution;

import java.awt.Dimension;
import java.nio.channels.SeekableByteChannel;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.unq.pconc.gameoflife.CellGrid;
import edu.unq.pconc.gameoflife.shapes.Shape;

/**
 * Contains the cellgrid, the current shape and the Game Of Life algorithm that changes it.
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

  /**
   * Contains the current, living shape.
   * It's implemented as a hashtable. Tests showed this is 70% faster than Vector.
   */
  private Hashtable currentShape;
  private Hashtable nextShape;

  private Enumeration e;

  /**
   * Every cell on the grid is a Cell object. This object can become quite large.
   */
  private Cell[][] grid;

  /**
   * Contructs a GameOfLifeGrid.
   * 
   * @param cellCols number of columns
   * @param cellRows number of rows
   */
  public GameOfLifeGrid() {
    this.cellCols = 0;
    this.cellRows = 0;
    currentShape = new Hashtable();
    nextShape = new Hashtable();

    grid = new Cell[cellCols][cellRows];
    for ( int c=0; c<cellCols; c++)
      for ( int r=0; r<cellRows; r++ )
        grid[c][r] = new Cell( c, r );


    // Cambios agregados:

    // Se crea el Threadpool donde se inician los Workers productores y los consumidores. Para producir celdas,
    // los productores van a necesitar saber en cuantas regiones tienen que dividir el tablero, y cuantas celdas tiene este.
    // Eso se obtiene facilmente haciendo la multiplicacionde columnas y filas

    // Por defecto dejamos la cantidad de regiones en 25.

    // Finalmente, el buffer que utilizara tiene un tamaño por defecto de 3 Regiones


    bufferParaTrabajadores 			  = new BufferDeRegiones(3,10);
    unThreadpool= new ThreadPool(bufferParaTrabajadores);

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
   */
  public synchronized void next() {
    Cell cell;
    int col, row;
    int neighbours;

    generations++;
    nextShape.clear();

    e = currentShape.keys();

    // Lo primero que se hace es dar las regiones al ThreadPool para que se encargue de generar los workers que van a poner las
    // regiones en el buffer, actuando como productores.

    // Esto va a poner a trabajar a levantar un thread y va a po
    bufferParaTrabajadores.resetEtapas();


    unThreadpool.gameOfLifeGrid(this);

    unThreadpool.celulasATrabajar(currentShape.size());
    unThreadpool.ponerProductoresATrabajar();
    unThreadpool.ponerConsumidoresATrabajar(this);


    /////////// //trabajo de worker: resetear las regiones//////////////
    // Esto estaba asi:
    //while ( e.hasMoreElements() ) {
    // cell = (Cell) e.nextElement();
    // cell.neighbour = 0;
    //}
    ////////////////////////////////////////////////////////////////////


    //cambias estrategia de workers y repetis lo mismo de arriba
    //e = currentShape.keys();
    //unThreadpool.ponerConsumidoresAAgregarVecinos(e, this);
    
    //e = currentShape.keys();
    
    // Add neighbours
    // You can't walk through an hashtable and also add elements. Took me a couple of ours to figure out. Argh!
    // That's why we have a hashNew hashtable.

    /*
    e = currentShape.keys();
    while ( e.hasMoreElements() ) {


      //////////////   trabajo de worker: agregar vecinos     /////
      cell = (Cell) e.nextElement();
      col = cell.col;
      row = cell.row;
      addNeighbour( col-1, row-1 );
      addNeighbour( col, row-1 );
      addNeighbour( col+1, row-1 );
      addNeighbour( col-1, row );
      addNeighbour( col+1, row );
      addNeighbour( col-1, row+1 );
      addNeighbour( col, row+1 );
      addNeighbour( col+1, row+1 );
      ////////////////////////////////////////////////////////


    }

    //cambias estrategia de workers.
    //e = currentShape.keys();
    //unThreadpool.ponerConsumidoresEliminarMuertos(e,this);
    */
    
    //e = currentShape.keys();
    
    /*
    // Bury the dead
    // We are walking through an enum from we are also removing elements. Can be tricky.
    e = currentShape.keys();
    while ( e.hasMoreElements() ) {



      ///////// trabajo de worker: Remover los muertos ///////
      cell = (Cell) e.nextElement();
      // Here is the Game Of Life rule (1):
      if ( cell.neighbour != 3 && cell.neighbour != 2 ) {
        currentShape.remove( cell );
      }
      /////////////////////////////////////////

    }

    //cambias estrategia de workers.
    
    // Bring out the new borns
    e = nextShape.keys();
    while ( e.hasMoreElements() ) {


      //////Trabajo de worker: crear las nuevas celdas ////////////////////////////
      cell = (Cell) e.nextElement();
      // Here is the Game Of Life rule (2):
      if ( cell.neighbour == 3 ) {
        setCell( cell.col, cell.row, true );

      //////////////////////////////////////////////
      }
    }


*/
  }


  /**
   * Adds a new neighbour to a cell.
   * 
   * @param col Cell-column
   * @param row Cell-row
   */
//  private synchronized void addNeighbour(int col, int row) {
  public synchronized void addNeighbour(int col, int row) {
    try {
      Cell cell = (Cell)nextShape.get( grid[col][row] );
      if ( cell == null ) {
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
   * @param col x-coordinate of cell
   * @param row y-coordinate of cell
   * @return value of cell
   */
  public synchronized boolean getCell( int col, int row ) {
    try {
      return currentShape.containsKey(grid[col][row]);
    } catch (ArrayIndexOutOfBoundsException e) {
      // ignore
    }
    return false;
  }

  /**
   * Set value of cell.
   * @param col x-coordinate of cell
   * @param row y-coordinate of cell
   * @param c value of cell
   */
  public synchronized void setCell( int col, int row, boolean c ) {
    try {
      Cell cell = grid[col][row];
      if ( c ) {
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
   * @return number of generations
   */
  public int getGenerations() {
    return generations;
  }
  
  /**
   * Get dimension of grid.
   * @return dimension of grid
   */
  public Dimension getDimension() {
    return new Dimension( cellCols, cellRows );
  }

  /**
   * Resize grid. Reuse existing cells.
   * @see edu.unq.pconc.gameoflife.CellGrid#resize(int, int)
   */
  public synchronized void resize(int cellColsNew, int cellRowsNew) {
    if ( cellCols==cellColsNew && cellRows==cellRowsNew )
      return; // Not really a resize
    // Create a new grid, reusing existing Cell's
    Cell[][] gridNew = new Cell[cellColsNew][cellRowsNew];
    for ( int c=0; c<cellColsNew; c++)
      for ( int r=0; r<cellRowsNew; r++ )
        if ( c < cellCols && r < cellRows )
          gridNew[c][r] = grid[c][r];
        else
          gridNew[c][r] = new Cell( c, r );
    // Copy existing shape to center of new shape
    int colOffset = (cellColsNew-cellCols)/2;
    int rowOffset = (cellRowsNew-cellRows)/2;
    Cell cell;
    Enumeration e;
    nextShape.clear();
    e = currentShape.keys();
    while ( e.hasMoreElements() ) {
      cell = (Cell) e.nextElement();
      int colNew = cell.col + colOffset;
      int rowNew = cell.row + rowOffset;
      try {
        nextShape.put( gridNew[colNew][rowNew], gridNew[colNew][rowNew] );
      } catch ( ArrayIndexOutOfBoundsException ex ) {
        // ignore
      }
    }
    // Copy new grid and hashtable to working grid/hashtable
    grid = gridNew;
    currentShape.clear();
    e = nextShape.keys();
    while ( e.hasMoreElements() ) {
      cell = (Cell) e.nextElement();
      currentShape.put( cell, cell );
    }
    cellCols = cellColsNew;
    cellRows = cellRowsNew;
  }
  
  public void setThreads(int threads) {
    unThreadpool.cambiarCantidadDeWorkers(threads);
  }

	public void removerDeCurrentShape(Cell cell) {
		currentShape.remove( cell );
	}

  public void resetearRegion(RegionDeTablero regionDeTablero) {

    for (int i = 0; i < regionDeTablero.cantidadCelulas ; i++) {
      //System.out.println("Cantidad de celdas" + regionDeTablero.cantidadCelulas);
      Cell cell = (Cell) e.nextElement();
      //System.out.println("Voy a trabajar una celda");
      //System.out.println(cell.toString());
      cell.neighbour = 0;
    }
  }

  public void agregarVecinosRegion(RegionDeTablero regionDeTablero) {
    int col, row;
    Cell cell;

    for (int i = 0; i < regionDeTablero.cantidadCelulas ; i++) {

      cell = (Cell) e.nextElement();
      //System.out.println("Agrego vecinos a la celda: "+ cell.toString());
      col = cell.col;
      row = cell.row;
      this.addNeighbour( col-1, row-1 );
      this.addNeighbour( col, row-1 );
      this.addNeighbour( col+1, row-1 );
      this.addNeighbour( col-1, row );
      this.addNeighbour( col+1, row );
      this.addNeighbour( col-1, row+1 );
      this.addNeighbour( col, row+1 );
      this.addNeighbour( col+1, row+1 );
    }
  }

  public void eliminarMuertosRegion(RegionDeTablero regionDeTablero) {
    for (int i = 0; i < regionDeTablero.cantidadCelulas ; i++) {
      Cell cell = (Cell) e.nextElement();

      // Here is the Game Of Life rule (1):
      if ( cell.neighbour != 3 && cell.neighbour != 2 ) {
        this.removerDeCurrentShape(cell);
      }
    }
  }

  public void nuevasCeldas(RegionDeTablero regionDeTablero) {
    for (int i = 0; i < regionDeTablero.cantidadCelulas ; i++) {
      Cell cell = (Cell) e.nextElement();
      // Here is the Game Of Life rule (2):
      if ( cell.neighbour == 3 ) {
        this.setCell( cell.col, cell.row, true );
      }
    }
  }

  public void setEnumCurrent() {
    e = currentShape.keys();
  }

  public void setEnumNext() {
    e = nextShape.keys();
  }

  public Hashtable getNextShape() {
    return nextShape;
  }

  public Hashtable getCurrentShape() {
    return currentShape;
  }
}

