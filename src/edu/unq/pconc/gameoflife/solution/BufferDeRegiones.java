package edu.unq.pconc.gameoflife.solution;

import java.util.Enumeration;
import java.util.Stack;

public class BufferDeRegiones {

	private Stack<RegionDeTablero> buffer;
	private Integer tamanioDelBuffer;
	private GameOfLifeGrid golg;

	public BufferDeRegiones(int elTamanioDelBuffer, GameOfLifeGrid unGolg) {

		tamanioDelBuffer = elTamanioDelBuffer;
		buffer = new Stack<RegionDeTablero>();
		golg = unGolg;
	}

	public synchronized void producirRegiones(Productor productor) throws InterruptedException {
		//System.out.println("Empeze a producir regiones");
		while (this.faltanTrabajarRegiones()) {
			if (!this.hayLugarEnElBuffer()) {
				wait();
			}

			if (this.faltanTrabajarRegiones() && hayLugarEnElBuffer()) {
				Cell cell = (Cell) golg.e.nextElement();
				RegionDeTablero regionDeTablero = productor.construirRegion(cell);

				buffer.push(regionDeTablero);
			}

			notifyAll();
		}
		//System.out.println("termine de producir Regiones");
	}

	public synchronized void resetearCeldas(Consumidor unWorker) throws InterruptedException {
		//System.out.println("Empeze a resetear regiones");
		while (this.faltanConsumirRegiones()) {
			if (!this.hayRegionEnBuffer()) {
				wait();
			}

			if (this.faltanConsumirRegiones() && this.hayRegionEnBuffer()) {
				RegionDeTablero regionDeTablero = buffer.pop();
				unWorker.resetearRegion(regionDeTablero,golg);
			}
			notifyAll();
		}
	//	System.out.println("termine de Resetear regiones");
	}

	public synchronized void agregarVecinos(Consumidor unWorker) throws InterruptedException {
		//System.out.println("Empeze a agregar Vecinos");
		while (this.faltanConsumirRegiones()) {
			if (!this.hayRegionEnBuffer()) {
				wait();
			}

			if (this.faltanConsumirRegiones() && this.hayRegionEnBuffer()) {
				RegionDeTablero regionDeTablero = buffer.pop();
				unWorker.agregarVecinosRegion(regionDeTablero, golg);
			}

			notifyAll();
		}
	}

	public synchronized void eliminarMuertos(Consumidor unWorker) throws InterruptedException {
		//System.out.println("Empeze a eliminar muertos");
		while (this.faltanConsumirRegiones()) {
			if (!this.hayRegionEnBuffer()) {
				wait();
			}

			if (this.faltanConsumirRegiones() && this.hayRegionEnBuffer()) {
				RegionDeTablero regionDeTablero = buffer.pop();
				unWorker.eliminarMuertosRegion(regionDeTablero, golg);
			}
			notifyAll();
		}
	}

	public synchronized void nuevasCeldas(Consumidor unWorker)  throws InterruptedException{
		//System.out.println("Empeze traer a los nacidos");
		while (this.faltanConsumirRegiones()){
            if(!this.hayRegionEnBuffer()){
                wait();
            }
            
            if (this.faltanConsumirRegiones() && this.hayRegionEnBuffer() ) {
            	RegionDeTablero regionDeTablero = buffer.pop();
            	unWorker.nuevasCeldas(regionDeTablero,golg);
			}
            notifyAll();
		}
		//System.out.println("Termine de traer a los nacidos");
	}

	private boolean hayRegionEnBuffer() {
		return !buffer.empty();
	}

	private boolean faltanConsumirRegiones() {
		return this.faltanTrabajarRegiones() || this.hayRegionEnBuffer();
	}

	private boolean hayLugarEnElBuffer() {
		return buffer.size() < tamanioDelBuffer;
	}

	private boolean faltanTrabajarRegiones() {
		return golg.e.hasMoreElements();
	}


}
