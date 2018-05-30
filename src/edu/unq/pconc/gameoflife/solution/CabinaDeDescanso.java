package edu.unq.pconc.gameoflife.solution;

public class CabinaDeDescanso {

	private Integer trabajadoresDescansando;
	private Integer cantidadDeTrabajadoresEnLaConstructora;
	private GameOfLifeGrid golg;

	private Integer turnoDeTrabajo;

	public CabinaDeDescanso(GameOfLifeGrid unGolg) {
		cantidadDeTrabajadoresEnLaConstructora = 0;
		trabajadoresDescansando = 0;
		golg = unGolg;
		turnoDeTrabajo = 0;
	}

	public synchronized void descansarHastaProximoTrabajo(Worker unWorker, Integer turno) throws InterruptedException {
		System.out.println("empece mi descanso del turno " + (turno - 1));
		trabajadoresDescansando++;

		if (this.soyElUltimoEnTerminarSuTrabajo()) {
			this.realizarlimpieza();
		}

		while (this.noEsElProximoTurno(turno)) {
			wait();
		}

		System.out.println("Sali a trabajar en el turno " + turno);

		notifyAll();
	}

	private boolean noEsElProximoTurno(Integer turno) {
		return turno != turnoDeTrabajo;
	}

	private void realizarlimpieza() {
		System.out.println("realize la limpieza");
	
		trabajadoresDescansando = 0;
		turnoDeTrabajo++;
		
		if (turnoDeTrabajo !=3) {
			golg.prepararLasLlavesParaEntrar();
			
		}
		else {
			System.out.println("Entre aca");
			golg.prepararLasLlavesParaIrse();
		}
		
	}

	private boolean soyElUltimoEnTerminarSuTrabajo() {
		return trabajadoresDescansando == cantidadDeTrabajadoresEnLaConstructora;
	}

	public void totalDeTrabajadores(Integer unTotalDeTrabajadores) {
		cantidadDeTrabajadoresEnLaConstructora = unTotalDeTrabajadores;
	}

	public void reset() {
		turnoDeTrabajo=0;
		
	}

}
