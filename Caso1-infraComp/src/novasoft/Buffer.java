package novasoft;

import java.util.ArrayList;

public class Buffer {
	
	private int max = 10;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	public void meter(Mensaje m) {
		boolean adentro = false;
		while(true) {
			synchronized (this) {
				if(mensajes.size() < max) {
					mensajes.add(m);
					adentro = true;
				}
				else {
					Thread.yield();
				}
			}
			if(adentro) {
				break;
			}
		}
	}

}
