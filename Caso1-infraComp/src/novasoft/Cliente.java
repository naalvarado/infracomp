package novasoft;

public class Cliente extends Thread {
	
	private int[] menss;
	public boolean termino;
	private Buffer buff;
	
	public Cliente(int[] numeroConsultas, Buffer pBu) {
		menss = numeroConsultas;
		termino = false;
		buff = pBu;
	}
	
	public void run() {
		
		for(int i = 0; i < menss.length; i++) {
			Mensaje mes = new Mensaje(menss[i]);
			try {
				buff.meter(mes, this);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		termino = true;
	}
}
