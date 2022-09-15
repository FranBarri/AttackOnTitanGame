package juego;

import java.awt.*;
import java.util.LinkedList;

import javax.sound.sampled.Clip;

import entorno.*;

public class Juego extends InterfaceJuego {
	private Mikasa mikasa;
	private PowerUp suero;
	private Curacion curacion;
	private LinkedList<Enemigo> titanes;
	private Obstaculo[] obstaculos;
	private Proyectil proyectil;
	private Entorno entorno;
	private Vida vidaMikasa;
	private Image imgFondo;
	private Image finDelJuego;
	
	private boolean pausar;
	private int contadorDeTicks;
	private int titanesEliminados;
	
	private AccionConTiempo reaparecerTitanes;
	private AccionConTiempo reaparecerSuero;
	private AccionConTiempo reaparecerCuracion;
 
	private Clip musicaDeFondo;
	private Clip disparo;
	private Clip disparoFallo;
	private Clip golpeMiksa;
	private Clip golpeTitan;
	private Clip agarrarVida;
	private Clip agarrarPowerUp;
	
	// CONSTANTES.
	private int SEGUNDOS_TITANES = 5;
	private int SEGUNDOS_SUERO = 10;
	private int SEGUNDOS_CURACION = 15;
	private int MAX_TITANES = 4;
	private int TICKS_POR_SEGUNDO = 50;  // Valor aproximado.
	private double DISTANCIA_SEGURA = 80;
	private double TITANES_PARA_GANAR = 12;
	
	public Juego() {
		this.entorno = new Entorno(this,
				"Attack on Titan - Grupo 4 - Barrientos - Dalmasso - Novella - Schenfeld- V0.01", 800, 600);
		imgFondo = Herramientas.cargarImagen("img/fondo.png");

		this.mikasa = new Mikasa(400, 300);
		this.titanes = new LinkedList<Enemigo>();
		this.vidaMikasa = new Vida(400,300);
		
		this.reaparecerTitanes = new AccionConTiempo(TICKS_POR_SEGUNDO * SEGUNDOS_TITANES);
		this.reaparecerSuero = new AccionConTiempo(TICKS_POR_SEGUNDO * SEGUNDOS_SUERO);
		this.reaparecerCuracion = new AccionConTiempo(TICKS_POR_SEGUNDO * SEGUNDOS_CURACION);
		
		this.pausar = false;
		this.contadorDeTicks = 0;
		this.titanesEliminados = 0;
		             
		this.musicaDeFondo = Herramientas.cargarSonido("sounds/fondo.wav");
		this.musicaDeFondo.loop(Clip.LOOP_CONTINUOUSLY);
		
		poblarEntorno();
		
		this.entorno.iniciar();
	}

	public void tick() {
		contadorDeTicks++;
		// Procesamiento de un instante de tiempo
		entorno.dibujarImagen(imgFondo, 400, 300, 0, 1);
		mikasa.dibujarse(entorno);

		procesarSuero();
		procesarProyectil();
		procesarVida();
		procesarCuracion();
		procesarTitanes();
		// Dibuja los obstaculos.
		for (Obstaculo obs : this.obstaculos) {
			obs.dibujar(entorno);
		}
		
		if (!pausar) {
			contadorDeTicks++;
			reaparecerTitanes();
			reaparecerSuero();
			reaparecerCuracion();
		} 
		procesarInput();
		estadoDelJuego();
		
		
		// contador de kyojines
		entorno.cambiarFont("Gill Sans Ultra Bold", 16, Color.WHITE);
		entorno.escribirTexto("KYOJINES ELIMINADOS: " + titanesEliminados, 30, 30);
	}

	public void procesarMovimientoJugador() {
		// Procesa solo los inputs relacionados con el movimiento.
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			this.mikasa.rotar(Math.toRadians(2));
		} else if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			this.mikasa.rotar(Math.toRadians(-2));
		} else if (entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			this.mikasa.avanzar(this.hitboxObstaculos());
		} else if (entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			this.mikasa.retroceder(this.hitboxObstaculos());
		}
		else if (entorno.sePresiono(entorno.TECLA_ESPACIO)
				&& !mikasa.esKiojina()) // Mikasa no puede disparar en modo Kiojina.
		{
			// Si no existe el proyectil lo crea.
			if (this.proyectil == null) {
				this.disparo = Herramientas.cargarSonido("sounds/disparo.wav");
				this.disparo.start();
				this.proyectil = new Proyectil(this.mikasa);
			} else {
				this.disparoFallo = Herramientas.cargarSonido("sounds/DisparoFallo.wav");    
				this.disparoFallo.start();
			}
		}
	}
	public void procesarInput() {
		// Procesa los inputs posibles.
		if (!pausar) {
			procesarMovimientoJugador();
		}
		// Pausar.
		if (entorno.sePresiono('p') || entorno.sePresiono('P')) {
	        // Cambia entre pausado y despausado.
			pausar = !pausar;
	    }
		// Salir del juego.
		else if (entorno.estaPresionada('c') || entorno.estaPresionada('C')) {
	        entorno.dispose();
	        System.exit(0);
	    }
	}
	
	public void procesarTitanes() {
		LinkedList<Enemigo> titanesARemover = new LinkedList<Enemigo>();
		for (Enemigo titan : this.titanes) {
			// Colisiones entre Mikasa y un titan.
			if (mikasa.getHitbox().hayColision(titan.getHitbox())) {
				if (mikasa.esKiojina()) {
					// Si Mikasa es kiojina entonces mata al titan con el que colisiona
					// y luego cambia a modo humano.
					titan.morir();
					mikasa.cambiarModo(); 
				} else {
					resetearTitanes();
					vidaMikasa.perderVida(mikasa);
					this.golpeMiksa = Herramientas.cargarSonido("sounds/MikasaOuch.wav");
					this.golpeMiksa.start();
				}	
			}
			// Procesa al titan.
			if (titan.estaVivo()) {
				titan.procesarse(entorno, mikasa, pausar, this.hitboxBloqueadoresDelTitan(titan));
			} else {
				this.golpeTitan = Herramientas.cargarSonido("sounds/TitanOuch.wav");
				this.golpeTitan.start();
				titanesARemover.add(titan);
				titanesEliminados++;
			}
		}
		// Esto evita errores producidos por modificar una lista mientras se itera.
		this.titanes.removeAll(titanesARemover);
		if(titanesARemover.size() > 0){
			reaparecerTitanes.setUltimaAccion(contadorDeTicks);
		}
	}

	public void procesarSuero() {
		if (suero != null) {
			suero.dibujarse(entorno);
			// mikasa toma el suero
			if (mikasa.getHitbox().hayColision(suero.getHitbox())) {
				this.agarrarPowerUp = Herramientas.cargarSonido("sounds/Powerup.wav"); 
				this.agarrarPowerUp.start();
				suero = null;
				mikasa.cambiarModo();
				reaparecerSuero.setUltimaAccion(contadorDeTicks);
			}
		}
	}
	
	public void procesarProyectil() {
		if (proyectil != null && !proyectil.fueDestruido()) {
			proyectil.procesarse(this.entorno, this.titanes, this.hitboxObstaculos(), this.pausar);
		} else {
			proyectil = null;
		}
	}
	
	public void procesarVida() {
		vidaMikasa.seguirMikasa(mikasa);
		vidaMikasa.dibujarse(entorno);
	}

	public void procesarCuracion(){
		if (curacion != null) {
			curacion.dibujarse(entorno);
			if (vidaMikasa.getGolpes() > 0 && mikasa.getHitbox().hayColision(curacion.getHitbox())) {				
				this.agarrarVida = Herramientas.cargarSonido("sounds/vida.wav");     
				this.agarrarVida.start();
				vidaMikasa.ganarVida(mikasa);
				curacion = null;
				reaparecerCuracion.setUltimaAccion(contadorDeTicks);
			}
		}
	}
	
	public void reaparecerTitanes(){
		if (this.titanes.size() < MAX_TITANES && reaparecerTitanes.esRealizable(contadorDeTicks)){
			agregarTitan();
		}
	}
	public void reaparecerSuero(){
		if (this.suero == null && reaparecerSuero.esRealizable(contadorDeTicks)){
			agregarSuero();
		}
	}
	
	public void reaparecerCuracion() {
		if (this.curacion == null && reaparecerCuracion.esRealizable(contadorDeTicks)){
			agregarCuracion();
		}
	}
	
	public LinkedList<Hitbox> hitboxObstaculos(){
		// Devuelve una lista con las hitbox de todos los obstaculos.
		LinkedList<Hitbox> hitboxes = new LinkedList<Hitbox>();
		for (Obstaculo obs : this.obstaculos) {
			hitboxes.add(obs.getHitbox());
		}
		return hitboxes;
	}
	public LinkedList<Hitbox> hitboxBloqueadoresDelTitan(Enemigo titan){
		// Esta funcion devuelve una lista con las hitbox de todo lo que bloquea a un titan.
		// Un titan es bloqueado por otros titanes y por obstaculos.
		LinkedList<Hitbox> hitboxes = new LinkedList<Hitbox>();
		for (Enemigo otroTitan : this.titanes) {
			// Queremos agregar las hitbox de los titanes que son distintos del titan pasado.
			if (titan != otroTitan) {				
				hitboxes.add(otroTitan.getHitbox());
			}
		}
		hitboxes.addAll(this.hitboxObstaculos());
		return hitboxes;
	}
	
	public void poblarEntorno() {
		// Selecciona un mapa aleatoriamente y coloca titanes y suero.
		this.obstaculos = Mapas.mapaAleatorio();
		//this.obstaculos = Mapas.mapaEspecifico(5);
		for (int i = 0; i < MAX_TITANES; i++) {
			agregarTitan();
		}
		agregarSuero();
	}

	public void agregarTitan() {
		// Prueba nuevas posiciones hasta que encuentra una valida.
		boolean agregado = false;
		while (!agregado){		
			double x = Math.random() * 800;
			double y = Math.random() * 600;
			Enemigo intentoEnemigo = new Enemigo(x, y);
			Hitbox hb = intentoEnemigo.getHitbox();
			if (Hitbox.chequearPosicionValida(x, y, hb, this.hitboxBloqueadoresDelTitan(intentoEnemigo)) 
					&& distanciaSeguraAMikasa(x, y)) {
				// Si el titan no colisiona con ningun obstaculo ni con mikasa lo agregamos
				this.titanes.add(intentoEnemigo);
				agregado = true;
			}
		}
	}
	
	public void resetearTitanes() {
		this.titanes = new LinkedList<Enemigo>();  // Resetea todos los titanes.
		// Agrega titanes nuevos.
		for (int i = 0; i < MAX_TITANES; i++) {
		    agregarTitan();
		}
	}

	public void agregarSuero() {
		// Prueba nuevas posiciones hasta que encuentra una valida.
		while (true) {
			double x = Math.random() * 800;
			double y = Math.random() * 600;
			PowerUp intentoSuero = new PowerUp(x, y);
			Hitbox hb = intentoSuero.getHitbox();
			if (Hitbox.chequearPosicionValida(x, y, hb, hitboxObstaculos())
					&& distanciaSeguraAMikasa(x, y)) {
				this.suero = intentoSuero;
				return;
			}
		}
	}
	
	public void agregarCuracion() {
		// Prueba nuevas posiciones hasta que encuentra una valida.
		while (true) {
			double x = Math.random() * 800;
			double y = Math.random() * 600;
			Curacion intentoCuracion = new Curacion(x, y);
			Hitbox hb = intentoCuracion.getHitbox();
			if (Hitbox.chequearPosicionValida(x, y, hb, hitboxObstaculos())
					&& distanciaSeguraAMikasa(x, y)) {
				this.curacion = intentoCuracion;
				return;
			}
		}
	}
	
	public boolean distanciaSeguraAMikasa(double x, double y) {
		return distSegura(x, y, mikasa.getX(), mikasa.getY()) &&
				distSegura(x, y, mikasa.getXDerecha(), mikasa.getY()) &&
				distSegura(x, y, mikasa.getX(), mikasa.getYInferior()) &&
				distSegura(x, y, mikasa.getXDerecha(), mikasa.getYInferior());
	}
	
	public boolean distSegura(double x1, double y1, double x2, double y2) {
		// Devuelve true si las coordenadas pasadas están a una distancia considerada segura.
		double terminoX = Math.pow((x1 - mikasa.getX()), 2);
		double terminoY = Math.pow((y1 - mikasa.getY()), 2);
		double distancia = Math.sqrt(terminoX + terminoY); // Distancia entre dos puntos
		return distancia > DISTANCIA_SEGURA;
	}
	
	public void estadoDelJuego() {
		// Maneja lo que pasa cuando ganas, perdes o pausas.
		if (titanesEliminados >= TITANES_PARA_GANAR) {
			ganaste();
		} else if (vidaMikasa.getGolpes() == 3) {
			perdiste();
		} else if (pausar){
			pausa();
		}
	}
	public void ganaste() {
		// PANTALLA FIN DEL JUEGO
		finDelJuego = Herramientas.cargarImagen("img/you_win.jpg");
		entorno.dibujarImagen(finDelJuego, 400, 300, 0, 1);
		pausar = true;
	}
	public void perdiste() {
		// PANTALLA FIN DEL JUEGO
		finDelJuego = Herramientas.cargarImagen("img/you_lose.jpg");
		entorno.dibujarImagen(finDelJuego, 400, 300, 0, 1);
		pausar = true;
	}
	public void pausa() {
		// No hace nada más que esperar a que el jugador aprete 'p' o 'c'.
		// Existe por si queremos implementar una pantalla de pausa en algún momento.
		return;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}