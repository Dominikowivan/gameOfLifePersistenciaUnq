package edu.unq.pconc.gameoflife.solution;

public class Productor extends Worker {

	public Productor(BufferDeRegiones bufferDeRegiones, CabinaDeDescanso cabinaDeDescanso) {
		super(bufferDeRegiones, cabinaDeDescanso);
	
	}
	
    @Override
    public void run() {
        try {
        	unBufferDeRegiones.producirRegiones(this);
        	unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,1);
        	unBufferDeRegiones.producirRegiones(this);
        	unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,2);
        	unBufferDeRegiones.producirRegiones(this);
        	unaCabinaDeDescanso.descansarHastaProximoTrabajo(this,3);
        	unBufferDeRegiones.producirRegiones(this);
        	
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

	public RegionDeTablero construirRegion(Cell cell) {
		 System.out.println("estoy creando una region para" + cell);
		return new RegionDeTablero(cell);
	}

}
