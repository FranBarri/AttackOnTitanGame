package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Vida {
	private boolean[] corazon;
	private double x;
	private double y;
	private double separacionCorazones;
	private int golpes;
	private Image CorazonVacio;
	private Image CorazonFull;
	
	// Constantes
    private static double OFFSET_X = 10;
    private static double OFFSET_Y = 30;
	
	public Vida(double x, double y) {
		this.corazon = new boolean[3];
		this.corazon[0] = true;
		this.corazon[1] = true;
		this.corazon[2] = true;
		this.golpes = 0;
		this.x = x;
		this.y = y;
		this.CorazonFull = Herramientas.cargarImagen("img/CorazonFull.png");
		this.CorazonVacio = Herramientas.cargarImagen("img/CorazonVacio.png");
		this.separacionCorazones = 25;
	}
	
	public void dibujarse(Entorno entorno) {
		double separacionActual = 0;
		for (int i = 0; i<corazon.length; i++) {
			if (corazon[i]) {
				entorno.dibujarImagen(this.CorazonFull, this.x + separacionActual, this.y, 0, 0.1);
				separacionActual += this.separacionCorazones;
			} else {
				if(!corazon[i])
				entorno.dibujarImagen(this.CorazonVacio, this.x + separacionActual, this.y, 0, 0.1);
				separacionActual += this.separacionCorazones;
			}
		}
	}

	public void perderVida(Mikasa mikasa) {
		this.golpes += 1;
		for (int i = 1; i<=this.golpes; i++) {
			corazon[i-1] = false;
		}
	}

	public void ganarVida(Mikasa mikasa) {
		if (this.golpes > 0) {			
			this.golpes -= 1;
		}
		for (int i = corazon.length-1; i >= 0; i--) {
			if (!corazon[i]) {
				corazon[i] = true;
				return;
			}		
		}
	}
	
	public int getGolpes() {
		return golpes;
	}
	
	public void seguirMikasa(Mikasa mikasa) {
		this.x = mikasa.getX() - OFFSET_X;
		this.y = mikasa.getY() - OFFSET_Y;
	}

}
