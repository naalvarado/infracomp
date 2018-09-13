package novasoft;

import java.util.ArrayList;

public class Buffer {

	public final static int max = 10;
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




	public synchronized Mensaje retirar(Servidor ser)
	{
		Mensaje mens=null;
		if(mensajes.size() > 0)
		{
			 mens=mensajes.get(mensajes.size()-1);
			mensajes.remove(mensajes.size()-1);
			nMensajes--;
			this.notifyAll();
		}
	return mens;
	}

}
