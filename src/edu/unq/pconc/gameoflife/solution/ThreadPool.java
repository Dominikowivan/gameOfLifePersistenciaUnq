package edu.unq.pconc.gameoflife.solution;

public class ThreadPool {
	
	  
    private Integer cantidadDeWorkersProductores;
    private Integer cantidadDeWorkersConsumidores;

    private BufferDeRegiones bufferDeRegiones;
    private CabinaDeDescanso unaCabinaDeDescanso;

    public ThreadPool(BufferDeRegiones unBuffer, CabinaDeDescanso cabinaDeDescanso){
        cantidadDeWorkersProductores = 1;
        cantidadDeWorkersConsumidores = 1;
        
        bufferDeRegiones = unBuffer;
        unaCabinaDeDescanso = cabinaDeDescanso;
       
        this.ponerSillasEnLaCabina();
    }
    
    private void ponerSillasEnLaCabina() {
        Integer unTotalDeTrabajadores = cantidadDeWorkersProductores+cantidadDeWorkersConsumidores;
        unaCabinaDeDescanso.totalDeTrabajadores(unTotalDeTrabajadores);
	}

	public void cambiarCantidadDeWorkers(int threads) {
        if (threads <= 2 ){
            cantidadDeWorkersProductores = 1;
            cantidadDeWorkersConsumidores = 1;
        }
        else{

            cantidadDeWorkersProductores = threads/2+ threads%2;
            cantidadDeWorkersConsumidores =  threads/2 ;
        }
        
        this.ponerSillasEnLaCabina();
    }
    
    public void ponerTrabajadoresATrabajar() {
    	//por cada uno de mis productores
        for (int i = 0; i < cantidadDeWorkersProductores ; i++) {

            //creo el worker que va a trabajar produciendo, y lo pongo a trabajar.
            Worker worker = new Productor(this.bufferDeRegiones,unaCabinaDeDescanso);
            worker.start();
        }
        
    	//por cada uno de misz consumidores
        for (int i = 0; i < cantidadDeWorkersConsumidores ; i++) {
      
            //creo el worker que va a trabajar consumiento, y lo pongo a trabajar.
            Worker worker = new Consumidor(this.bufferDeRegiones,unaCabinaDeDescanso);
            worker.start();
        }
    }



}
