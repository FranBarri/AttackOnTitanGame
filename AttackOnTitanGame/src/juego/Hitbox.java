package juego;

import java.util.LinkedList;

public class Hitbox {
	private double x;
	private double y;
	private double ancho;
	private double largo;
	private double x_derecha;
	private double y_inferior;

	// CONSTANTES.
	private static double PARED_NORTE = 55;
	private static double PARED_OESTE = 55;
	private static double PARED_SUR = 600;
	private static double PARED_ESTE = 800;


	public Hitbox(double x, double y, double ancho, double largo) {
		this.ancho = ancho;
		this.largo = largo;
		this.actualizarXeY(x, y);
	}

	public void actualizarXeY(double x, double y) {
		this.x = x;
		this.y = y;
		this.x_derecha = this.x + ancho;
		this.y_inferior = this.y + largo;
	}
	
	public boolean hayColision(Hitbox otra) {
		// Si ninguna de estas condiciones se cumple entonces hay una colision.
		return !(this.x > otra.x_derecha ||
				this.x_derecha < otra.x  ||
				this.y > otra.y_inferior ||
				this.y_inferior < otra.y);
	}

	public static boolean chequearPosicionValida(double x, double y, Hitbox hbActual,
													LinkedList<Hitbox> bloqueadores) {
		// Se asegura que las posiciones pasadas no colisionen con obstaculos ni
		// paredes.
		Hitbox intentoHitbox = new Hitbox(x, y, hbActual.ancho, hbActual.largo);
		for (Hitbox bloqueador : bloqueadores) {
			if (bloqueador.hayColision(intentoHitbox)) {
				return false;
			}
		}
		return noChocaConParedes(intentoHitbox);
	}

	public static boolean noChocaConParedes(Hitbox intentoHitbox) {
		return (intentoHitbox.x > PARED_OESTE) && (intentoHitbox.y > PARED_NORTE)
				&& (intentoHitbox.x_derecha < PARED_ESTE) && (intentoHitbox.y_inferior < PARED_SUR);
	}
}
