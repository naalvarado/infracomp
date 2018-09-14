package novasoft;

public class Servidor extends Thread {
	
	private Buffer buff;
	
	public Servidor(Buffer pBuffer){
		buff = pBuffer;
	}
	
	
	public void run(){
		while(true){
			Mensaje nuevo=buff.retirar();
			if(nuevo ==null)
			{
				synchronized (buff) {
					try
					{
						buff.wait();
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				nuevo.setRespuesta(nuevo.getContenido()+1);
				synchronized (nuevo) {
					nuevo.notifyAll();
					
				}
			}
			yield();
		}
	}
}
