package juego;

public class AccionConTiempo {
	private int ultimaAccion;
	private int tsEntreAcciones;
	
	public AccionConTiempo(int tsEntreAcciones) {
		this.ultimaAccion = 0;
		this.tsEntreAcciones = tsEntreAcciones;
	}
	
	public boolean esRealizable(int tickActual) {
		// Se asume que en caso de devolver true se realizara la accion.
		if ((tickActual - ultimaAccion) >= tsEntreAcciones || 
				this.ultimaAccion == 0) // Si es la primera vez que se realiza la accion devuelve true.
		{
			this.ultimaAccion = tickActual;
			return true;
		} 
		return false;
	}
	
	public int getUltimaAccion(){
		return ultimaAccion;
	}
	
	public void setUltimaAccion(int ultima){
		this.ultimaAccion = ultima;
	}
}
