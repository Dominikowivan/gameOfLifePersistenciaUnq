package edu.unq.pconc.gameoflife.solution;

public class Worker extends Thread {

	public BufferDeRegiones unBufferDeRegiones;
	public CabinaDeDescanso unaCabinaDeDescanso;
	 
	public Worker(BufferDeRegiones bufferDeRegiones, CabinaDeDescanso cabinaDeDescanso) {
		unBufferDeRegiones  = bufferDeRegiones;
		unaCabinaDeDescanso = cabinaDeDescanso; 
	}
	
    @Override
    public void run() {
     
    }



}
