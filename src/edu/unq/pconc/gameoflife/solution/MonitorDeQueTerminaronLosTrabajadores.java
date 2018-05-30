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
		notifyAll();
	}
	
	public synchronized void esperarAQueTerminenTrabajadores() throws InterruptedException{
		while(noTerminaronLosTrabajadores()){
			
			wait();
		}
		
		workersQueTerminaron=0;
	}

	private boolean noTerminaronLosTrabajadores() {
		return workersQueTerminaron != cantidadDeWorkersQueTengo;
	}

	public void totalDeTrabajadores(Integer unTotalDeTrabajadores) {
		
		cantidadDeWorkersQueTengo = unTotalDeTrabajadores;		
	}

}
