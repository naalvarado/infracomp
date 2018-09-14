package novasoft;

import java.util.ArrayList;


public class Buffer {

	private int max;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	private ArrayList<Cliente> clientesA = new ArrayList<Cliente>();
	private int nMensajes;

	private static Buffer buffer;

	public Buffer() {
		mensajes = new ArrayList<Mensaje>();
		clientesA = new ArrayList<Cliente>();
		nMensajes = 0;
	}	
	
	public void setBuffSize(int s)
	{
		max=s;
	}

	public static Buffer getBuffer() {
		if(buffer == null) {
			buffer = new Buffer();
		}
		return buffer;
	}
	public synchronized boolean meter(Mensaje m, Cliente cli)  {
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



	public synchronized Mensaje retirar()
	{
		Mensaje retirar=null;
		{
			if(nMensajes>0)
			{
				retirar=mensajes.get(mensajes.size()-1);
				mensajes.remove(mensajes.size()-1);
				nMensajes--;
			}
			return retirar;
		}
	}
	public synchronized boolean chapCliente(Cliente clienteR) {
		return clientesA.remove(clienteR);
	}


}
