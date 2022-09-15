package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Curacion {
	private double x;
	private double y;
	private Hitbox hitbox;
	
	private static double ANCHO = 15;
	private static double LARGO = 15;
	
	private static Image curacion;

	public Curacion(double x, double y) {
		this.x = x;
		this.y = y;
		this.hitbox = new Hitbox(x, y, ANCHO, LARGO);
		
		curacion = Herramientas.cargarImagen("img/pavo.png");
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarImagen(curacion, this.x, this.y, 0, 0.1);
	}

	public Hitbox getHitbox() {
		return hitbox;
	}

}
