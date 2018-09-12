package novasoft;

public class Cliente extends Thread {
	
	private int[] menss;
	private boolean termino;
	
	public Cliente(int[] numeroConsultas) {
		menss = numeroConsultas;
		termino = false;
	}
	
	public void run() {
		
		for(int i = 0; i < menss.length; i++) {
			Mensaje mes = new Mensaje(menss[i]);
		}
		
	}
}
