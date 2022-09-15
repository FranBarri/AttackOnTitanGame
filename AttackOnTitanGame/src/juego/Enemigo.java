package juego;

import java.awt.Image;
import java.util.LinkedList;

import entorno.Entorno;
import entorno.Herramientas;

public class Enemigo {
	private boolean vivo;
	private double x;
	private double y;
	private Hitbox hitbox;
	private boolean falloVertical;
	private boolean hizoMovHAbajo;
	private boolean falloHorizontal;
	private boolean hizoMovHIzq;

	// CONSTANTES.
	private double VELOCIDAD = 0.2;
	private static double ANCHO = 70;
    private static double LARGO = 110;
    private static double OFFSET_X = 35;
    private static double OFFSET_Y = 50;
	
	Image sprite;

	public Enemigo(double x, double y) {
		this.x = x;
		this.y = y;
		this.hitbox = new Hitbox(x, y, ANCHO, LARGO);
		this.vivo = true;
		sprite = Herramientas.cargarImagen("img/titan.png");
		this.hizoMovHAbajo = true;
		this.falloVertical = false;
		this.hizoMovHIzq = true;
		this.falloHorizontal = false;
	}

	private void dibujarse(Entorno entorno) {
		entorno.dibujarImagen(sprite, this.x + OFFSET_X, this.y + OFFSET_Y, 0, 0.05);
	}

	private void moverseHaciaMikasa(Mikasa mikasa, LinkedList<Hitbox> bloqueadores) {		
		moverseEnHorizontal(mikasa.getX(), bloqueadores);
		moverseEnVertical(mikasa.getY(), bloqueadores);
	}

	private boolean confirmarMovimiento(double intentoX, double intentoY, 
									LinkedList<Hitbox> bloqueadores) {
		return (Hitbox.chequearPosicionValida(intentoX, intentoY, this.hitbox, bloqueadores));
	}
	
	private void moverseEnHorizontal(Double objetivoX, LinkedList<Hitbox> bloqueadores) {
		boolean izquierda = this.hizoMovHIzq;
		double intentoX = this.x;
		if (!this.falloVertical) {
			if (objetivoX > this.x) {
				intentoX = this.x + VELOCIDAD;
				izquierda = false;
			} else {
				intentoX = this.x - VELOCIDAD;
				izquierda = true;
			}
			if (confirmarMovimiento(intentoX, this.y, bloqueadores)) {
				moverse(intentoX, this.y);
				this.hizoMovHIzq = izquierda;
				this.falloHorizontal = false;
				return;
			} 
			this.falloHorizontal = true;
		}
		else {
			if (this.hizoMovHIzq) { intentoX = this.x - (VELOCIDAD*1.5); } 
			else { intentoX = this.x + (VELOCIDAD*1.5); }
		}
		
		if (confirmarMovimiento(intentoX, this.y, bloqueadores)) {moverse(intentoX, this.y);}
	}
	
	private void moverseEnVertical(Double objetivoY, LinkedList<Hitbox> bloqueadores) {
		boolean abajo = this.hizoMovHAbajo;
		double intentoY = this.y;
		if (!this.falloHorizontal) {			
			if (objetivoY > this.y) {
				intentoY = this.y + VELOCIDAD;
				abajo = true;
			} else {
				intentoY = this.y - VELOCIDAD;
				abajo = false;
			}
			if(confirmarMovimiento(this.x, intentoY, bloqueadores)) {
				moverse(this.x, intentoY);
				this.hizoMovHAbajo = abajo;
				this.falloVertical = false;
				return;
			}
			this.falloVertical = true;
		}
		else {
			if (this.hizoMovHAbajo) { intentoY = this.y + (VELOCIDAD*1.5); } 
			else { intentoY = this.y - (VELOCIDAD*1.5); }
		}
		
		if (confirmarMovimiento(this.x, intentoY, bloqueadores)) {moverse(this.x, intentoY);}
	}
	
	
	private void moverse(double x, double y) {
		this.x = x;
		this.y = y;
		this.hitbox.actualizarXeY(this.x, this.y);
	}
	
	public void procesarse(Entorno entorno, Mikasa mikasa, 
						boolean pausado, LinkedList<Hitbox> bloqueadores) {
		this.dibujarse(entorno);
		if (!pausado) {			
			this.moverseHaciaMikasa(mikasa, bloqueadores);
		}
	}
	
	public void morir() {
		this.vivo = false;
	}
	public boolean estaVivo() {
		return vivo;
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}

	public Hitbox getHitbox() {
		return hitbox;
	}
}