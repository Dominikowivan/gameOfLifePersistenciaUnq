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
		
		while (this.faltanTrabajarRegiones()) {
			if (!this.hayLugarEnElBuffer()) {
				wait();
			}

			if (puedoCrearRegion()) {
				Cell cell = (Cell) golg.e.nextElement();
				RegionDeTablero regionDeTablero = productor.construirRegion(cell);

				buffer.push(regionDeTablero);
			}

			notifyAll();
		}
		
	}

	private boolean puedoCrearRegion() {
		return this.faltanTrabajarRegiones() && hayLugarEnElBuffer();
	}

	public synchronized void consumirRegion(Consumidor unWorker) throws InterruptedException {
		
		while (this.faltanConsumirRegiones()) {
			if (!this.hayRegionEnBuffer()) {
				wait();
			}

			if (puedoConsumirRegion()) {
				RegionDeTablero regionDeTablero = buffer.pop();
				unWorker.consumir(regionDeTablero,golg);
			}
			notifyAll();
		}
		
	}

	private boolean puedoConsumirRegion() {
		return this.faltanConsumirRegiones() && this.hayRegionEnBuffer();
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
