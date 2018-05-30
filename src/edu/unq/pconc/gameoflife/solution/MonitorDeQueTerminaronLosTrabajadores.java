package edu.unq.pconc.gameoflife.solution;

public class MonitorDeQueTerminaronLosTrabajadores {

	public Integer workersQueTerminaron;
	public Integer cantidadDeWorkersQueTengo;
	
	
	public MonitorDeQueTerminaronLosTrabajadores(){
		workersQueTerminaron = 0;
		cantidadDeWorkersQueTengo = 0;
	}
	
	public synchronized void termine(Worker unWorker) {
		workersQueTerminaron++;
		System.out.println("Termine de trabajar");
		notifyAll();
	}
	
	public synchronized void esperarAQueTerminenTrabajadores() throws InterruptedException{
		while(noTerminaronLosTrabajadores()){
			System.out.println("Entre a Esperar que terminen los trabajadores");
			wait();
		}
		System.out.println("Sali a Esperar que terminen los trabajadores");
		workersQueTerminaron=0;
	}

	private boolean noTerminaronLosTrabajadores() {
		return workersQueTerminaron != cantidadDeWorkersQueTengo;
	}

	public void totalDeTrabajadores(Integer unTotalDeTrabajadores) {
		
		cantidadDeWorkersQueTengo = unTotalDeTrabajadores;		
	}

}
