package juego;

import java.util.Random;

public class Mapas {
	// Un "mapa" es un arreglo de obstaculos.
	
	// Constante con todos los mapas creados.
	private static Obstaculo[][] MAPAS = {
			 // Mapa 0
			 {
				 new Obstaculo(150, 150, 0),
				 new Obstaculo(600, 150, 0),
				 new Obstaculo(150, 400, 0),
				 new Obstaculo(600, 400, 0)
			 },
			 // Mapa 1
			 {
				 new Obstaculo(290, 170, 0),
				 new Obstaculo(360, 170, 0),
				 new Obstaculo(430, 170, 0),
				 new Obstaculo(300, 400, 1),
				 new Obstaculo(370, 400, 1),
				 new Obstaculo(440, 400, 1)
			 },
			 // Mapa 2
			 {
				 new Obstaculo(200, 220, 0),
				 new Obstaculo(200, 290, 0),
				 new Obstaculo(200, 360, 0),
				 new Obstaculo(550, 220, 0),
				 new Obstaculo(550, 290, 0),
				 new Obstaculo(550, 360, 0)
			 },
			 // Mapa 3
			 {
				 new Obstaculo(350, 180, 1),
				 new Obstaculo(200, 360, 0),
				 new Obstaculo(500, 360, 0),
				 new Obstaculo(400, 180, 1)
			 },
			 // Mapa 4
			 {
				 new Obstaculo(100, 250, 0),
				 new Obstaculo(250, 250, 0),
				 new Obstaculo(470, 250, 0),
				 new Obstaculo(620, 250, 0)
			 },
			 // Mapa 5
			 {
				 new Obstaculo(340, 190, 1),
				 new Obstaculo(340, 223, 1),
				 new Obstaculo(410, 190, 1), 
				 new Obstaculo(410, 223, 1), 
				 new Obstaculo(193, 470, 0),
				 new Obstaculo(530, 470, 0)
			 },
			 // Mapa 6                      
			 {                              
				 new Obstaculo(400, 444, 1),
				 new Obstaculo(450, 444, 1),
				 new Obstaculo(500, 444, 1),
				 new Obstaculo(200, 222, 1),
				 new Obstaculo(250, 222, 1) 
			 },
			 // Mapa 7
			 {                              
				 new Obstaculo(400, 150, 1),
				 new Obstaculo(450, 150, 1),
				 new Obstaculo(500, 150, 1),
				 new Obstaculo(225, 350, 0),
				 new Obstaculo(200, 444, 1),
				 new Obstaculo(250, 444, 1) 
			 },
			 // Mapa 8 (Perdon)
			 {                              
				 new Obstaculo(140, 200, 1),
				 new Obstaculo(140, 240, 1),
				 new Obstaculo(140, 290, 1),
				 new Obstaculo(140, 340, 1),
				 new Obstaculo(140, 390, 1),
				 new Obstaculo(280, 200, 1),
				 new Obstaculo(280, 240, 1),
				 new Obstaculo(280, 290, 1),
				 new Obstaculo(280, 340, 1),
				 new Obstaculo(280, 390, 1),
				 new Obstaculo(500, 200, 1),
				 new Obstaculo(500, 240, 1),
				 new Obstaculo(500, 290, 1),
				 new Obstaculo(500, 340, 1),
				 new Obstaculo(500, 390, 1),
				 new Obstaculo(630, 200, 1),
				 new Obstaculo(630, 240, 1),
				 new Obstaculo(630, 290, 1),
				 new Obstaculo(630, 340, 1),
				 new Obstaculo(630, 390, 1) 
			 }
	 };
	 
	 public static Obstaculo[] mapaAleatorio() {
		 int indiceAleatorio = new Random().nextInt(MAPAS.length);
		 return MAPAS[indiceAleatorio];
	 }
	 
	 public static Obstaculo[] mapaEspecifico(int indice) {
		 // Solo usarlo para probar nuevos "mapas".
		 return MAPAS[indice];
	 }
}
