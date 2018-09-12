package novasoft;

import java.util.ArrayList;

public class Buffer {
	
	private int max = 10;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	private ArrayList<Cliente> clientesA = new ArrayList<Cliente>();
	
	public void meter(Mensaje m, Cliente cli) throws InterruptedException {
		boolean adentro = false;
		while(true) {
			synchronized (this) {
				if(mensajes.size() < max) {
					mensajes.add(m);
					adentro = true;
					this.notifyAll();
				}
				else {
					Thread.yield();
				}
			}
			if(adentro) {
				cli.wait();
			}
		}
	}
	
	public void retirar(Servidor ser){
		while(true){
			if(mensajes.size() > 0){
				mensajes.remove(mensajes.size()-1);
				this.notifyAll();
			}
			else{
				ser.yield();
			}
		}
	}

}
