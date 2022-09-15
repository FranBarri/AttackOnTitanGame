package juego;

import java.awt.Image;
import java.util.LinkedList;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {
	private double x;
	private double y;
	private double rotacion;
	private Hitbox hitbox;
	private boolean modoKiojina;
	private Image aspectoActual;
	private static Image MODO_HUMANA;
	private static Image MODO_KIOJINA;

	// CONSTANTES.
	private static double ANCHO = 30;
    private static double LARGO = 30;
    private static double VELOCIDAD = 2.4;
    private static double OFFSET_X = 10;
    private static double OFFSET_Y = 20;

	public Mikasa(double x, double y) {
		this.x = x;
		this.y = y;
		this.rotacion = 0;
		this.hitbox = new Hitbox(x, y, ANCHO, LARGO);
		modoKiojina = false;
		
		MODO_HUMANA = Herramientas.cargarImagen("img/mikasa.png");
		MODO_KIOJINA = Herramientas.cargarImagen("img/mikasa_mala.png");
		
		aspectoActual = MODO_HUMANA;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarImagen(aspectoActual, this.x + OFFSET_X, this.y + OFFSET_Y, this.rotacion, 0.04);
	}

	public void rotar(double direccion) {
		// Rota al personaje sobre su eje.
		this.rotacion += direccion;
	}

	public void avanzar(LinkedList<Hitbox> bloqueadores) {
		// Mueve al personaje en la dirección en la que esta viendo.
		double intentoX = this.x + Math.cos(this.rotacion) * VELOCIDAD;
		double intentoY = this.y + Math.sin(this.rotacion) * VELOCIDAD;

		// Solo moverse si el movimiento no causa una colision.
		if (Hitbox.chequearPosicionValida(intentoX, intentoY, this.hitbox,bloqueadores)) {
			this.x = intentoX;
			this.y = intentoY;
			this.hitbox.actualizarXeY(this.x, this.y);
		}
	}

	public void retroceder(LinkedList<Hitbox> bloqueadores) {
		// Mueve al personaje en la dirección opuesta a la que esta viendo.
		double intentoX = this.x - Math.cos(this.rotacion) * VELOCIDAD;
		double intentoY = this.y - Math.sin(this.rotacion) * VELOCIDAD;

		// Solo moverse si el movimiento no causa una colision.
		if (Hitbox.chequearPosicionValida(intentoX, intentoY, this.hitbox, bloqueadores)) {
			this.x = intentoX;
			this.y = intentoY;
			this.hitbox.actualizarXeY(this.x, this.y);
		}
	}

	public boolean esKiojina() {
		return modoKiojina;
	}
	
	public void cambiarModo() {
		// Cambia a Mikasa entre humana y kiojina.
		if (modoKiojina) {			
			aspectoActual = MODO_HUMANA;
			modoKiojina = false;
		} else {
			aspectoActual = MODO_KIOJINA;
			modoKiojina = true;
		}
	}
	
	public double getX() {
		return x;
	}
	public double getXDerecha() {
		return x + ANCHO;
	}

	public double getY() {
		return y;
	}
	public double getYInferior() {
		return y + LARGO;
	}
	
	public Hitbox getHitbox() {
		return hitbox;
	}

	public double getRotacion() {
		return rotacion;
	}
}