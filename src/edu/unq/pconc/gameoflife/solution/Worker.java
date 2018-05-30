package edu.unq.pconc.gameoflife.solution;

public class Worker extends Thread {

	public BufferDeRegiones unBufferDeRegiones;
	public CabinaDeDescanso unaCabinaDeDescanso;
	public MonitorDeQueTerminaroLosTrabajadores monitorTrabajador;
	 
	public Worker(BufferDeRegiones bufferDeRegiones, CabinaDeDescanso cabinaDeDescanso, MonitorDeQueTerminaroLosTrabajadores unMonitorTrabajador) {
		unBufferDeRegiones  = bufferDeRegiones;
		unaCabinaDeDescanso = cabinaDeDescanso; 
		monitorTrabajador = unMonitorTrabajador;
	}
	
    @Override
    public void run() {
     
    }



}
