package juego;

import java.awt.Color;
import java.util.LinkedList;

import entorno.Entorno;

public class Proyectil {
	private double x;
	private double y;
	private double rotacion;
	private boolean existe;
	private Hitbox hitbox;

	// CONSTANTES.
	private static double ANCHO = 30;
	private static double LARGO = 30;
	
	public Proyectil(Mikasa mikasa) {
		this.x = mikasa.getX();
		this.y = mikasa.getY();
		this.rotacion = mikasa.getRotacion();
		this.existe = true;
		this.hitbox = new Hitbox(this.x, this.y, ANCHO, LARGO);
	}

	private void moverse(LinkedList<Enemigo> titanes, LinkedList<Hitbox> obstaculos) {
		double intentoX = this.x + Math.cos(this.rotacion) * 3;
		double intentoY = this.y + Math.sin(this.rotacion) * 3;
		// Solo moverse si el movimiento no causa una colision.
		if (Hitbox.chequearPosicionValida(intentoX, intentoY, this.hitbox, obstaculos)) {
			this.x = intentoX;
			this.y = intentoY;
			this.hitbox.actualizarXeY(this.x, this.y);
		} else {
			this.existe = false;
		}
		matarTitanes(titanes);
	}
	
	private void matarTitanes(LinkedList<Enemigo> titanes) {
		for (Enemigo titan : titanes) { 
			// Si choca contra un titan lo mata.
			if (titan.getHitbox().hayColision(this.hitbox)) {
				titan.morir();
				this.existe = false;
			}
		}
	}
	
	public boolean fueDestruido() {
		return !existe;
	}
	
	public void procesarse(Entorno entorno, LinkedList<Enemigo> titanes, LinkedList<Hitbox> obstaculos,
			boolean pausado) {
		entorno.dibujarCirculo(this.x, this.y, 30, Color.black);
		if (!pausado) {			
			this.moverse(titanes, obstaculos);
		}
	}
}
