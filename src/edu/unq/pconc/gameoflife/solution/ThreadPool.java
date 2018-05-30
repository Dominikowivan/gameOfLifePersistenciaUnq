package edu.unq.pconc.gameoflife.solution;

public class ThreadPool {
	
	  
    private Integer cantidadDeWorkersProductores;
    private Integer cantidadDeWorkersConsumidores;

    private BufferDeRegiones                      bufferDeRegiones;
    private CabinaDeDescanso                      unaCabinaDeDescanso;
	private MonitorDeQueTerminaronLosTrabajadores monitorTrabajador;

    
    public ThreadPool(BufferDeRegiones unBuffer, CabinaDeDescanso cabinaDeDescanso, MonitorDeQueTerminaronLosTrabajadores unMonitorTrabajador){
        cantidadDeWorkersProductores  = 1;
        cantidadDeWorkersConsumidores = 1;
        
        bufferDeRegiones    = unBuffer;
        unaCabinaDeDescanso = cabinaDeDescanso;
        monitorTrabajador   = unMonitorTrabajador;
        this.cantidadTrabajadores();
    }
    
    private void cantidadTrabajadores() {
        Integer unTotalDeTrabajadores = cantidadDeWorkersProductores+cantidadDeWorkersConsumidores;
        unaCabinaDeDescanso.totalDeTrabajadores(unTotalDeTrabajadores);
        monitorTrabajador  .totalDeTrabajadores(unTotalDeTrabajadores);
	}

	public void cambiarCantidadDeWorkers(int threads) {
        
        cantidadDeWorkersProductores  = threads/2+ threads%2;
        cantidadDeWorkersConsumidores =  threads/2 ;
        
        
        this.cantidadTrabajadores();
    }
    
    public void ponerTrabajadoresATrabajar() {
    	//por cada uno de mis productores
        for (int i = 0; i < cantidadDeWorkersProductores ; i++) {

            //creo el worker que va a trabajar produciendo, y lo pongo a trabajar.
            Worker worker = new Productor(this.bufferDeRegiones,unaCabinaDeDescanso,monitorTrabajador);
            worker.start();
        }
        
    	//por cada uno de misz consumidores
        for (int i = 0; i < cantidadDeWorkersConsumidores ; i++) {
      
            //creo el worker que va a trabajar consumiento, y lo pongo a trabajar.
            Worker worker = new Consumidor(this.bufferDeRegiones,unaCabinaDeDescanso,monitorTrabajador);
            worker.start();
        }
    }



}
