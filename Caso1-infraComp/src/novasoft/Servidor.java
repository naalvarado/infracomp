package novasoft;

public class Servidor extends Thread {
	
	private Buffer buff;
	
	public Servidor(Buffer pBu){
		buff = pBu;
	}
	
	public void run(){
		while(true){
			buff.retirar(this);
		}
	}
}
