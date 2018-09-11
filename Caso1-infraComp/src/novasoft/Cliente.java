package novasoft;

public class Cliente extends Thread {
	
	private int[] menss;
	private boolean termino;
	
	public Cliente(int[] numeroConsultas) {
		menss = numeroConsultas;
		termino = false;
	}
	
	public void run() {
		
	}
}
