package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {

	private double x;
	private double y;
	private Hitbox hitbox;
	private int tipo;
	
	// CONSTANTES.
	// Casa = 0. Arbol = 1.
	private static double[] ANCHOS = {65, 30};
    private static double[] LARGOS = {100, 40};
    private static double[] ESCALAS = {0.7, 0.3};
    private static Image[] APARIENCIAS = new Image[2];
    private static double[] OFFSETS_X = {30, 15};
    private static double[] OFFSETS_Y = {40, 15};
	
	public Obstaculo(double x, double y, int tipo) {
		// Casa.
		APARIENCIAS[0] = Herramientas.cargarImagen("img/casa.png").getScaledInstance(250, 350, 1);
		// Arbol.
		APARIENCIAS[1] = Herramientas.cargarImagen("img/arbol.png");
		
		this.x = x;
		this.y = y;
		this.tipo = tipo;
		this.hitbox = new Hitbox(x, y, ANCHOS[tipo], LARGOS[tipo]);		
	}

	public void dibujar(Entorno e) {
		e.dibujarImagen(APARIENCIAS[this.tipo], x + OFFSETS_X[tipo], y + OFFSETS_Y[tipo], 0, ESCALAS[tipo]);
	}

	public Hitbox getHitbox() {
		return hitbox;
	}
}