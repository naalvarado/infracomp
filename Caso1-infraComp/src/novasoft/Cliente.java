package novasoft;

public class Cliente extends Thread {

	private Mensaje[] menss;
	public boolean termino;
	private Buffer buff;
	private int idCli;

	public Cliente(Buffer pBu, int nMensajes,int idCliente) {
		menss = new Mensaje[nMensajes];
		buff=pBu;
		idCli=idCliente;
		for(int i=0;i<menss.length;i++)
		{
			menss[i]=new Mensaje();
			menss[i].setContenido(i+1);
		}
	}

	public void run() {
		for(int i = 0; i < menss.length; i++) {
			while(buff.meter(menss[i], this)==false)
			{
				System.out.println("El buffer no esta aceptando solicitudes por el momento");
				yield();
			}
			synchronized (menss[i])
			{
				try
				{
					System.out.println("Mensaje" +menss[i].getContenido()+"entro al sistema");
					while(menss[i].getRespuesta()==0)
						menss[i].wait();
					System.out.println("El mensaje"+menss[i].getContenido()+"respondio:"+menss[i].getRespuesta());
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		if(buff.chapCliente(this))
		{
			System.out.println("El cliente "+idCli+"ha salido del sistema");
		}

	}
}
