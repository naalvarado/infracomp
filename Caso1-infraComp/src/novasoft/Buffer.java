package novasoft;

import java.util.ArrayList;

public class Buffer {
	
	private int max = 10;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	private ArrayList<Cliente> clientesA = new ArrayList<Cliente>();
	private int nMensajes;
	
	private static Buffer buffer;
	
	private Buffer() {
		mensajes = new ArrayList<Mensaje>();
		clientesA = new ArrayList<Cliente>();
		nMensajes = 0;
	}	
	
	public static Buffer getBuffer() {
		if(buffer == null) {
			buffer = new Buffer();
		}
		return buffer;
	}
	public synchronized boolean meter(Mensaje m, Cliente cli) throws InterruptedException {
		boolean adentro = false;
		if(nMensajes>max)
			adentro=false;
		else
		{
			mensajes.add(m);
			nMensajes++;
			if(!clientesA.contains(cli))
			{
				clientesA.add(cli);
			}
			adentro=true;
			notifyAll();
			
		}
		return adentro;
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
