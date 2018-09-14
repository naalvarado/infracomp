package novasoft;

import java.io.*;

public class main {
	
	public static void main(String[] args){
		int numServs=0;
		int numCli=0;
		int tamBuff=0;
		
		File file = new File("properties.txt");
		BufferedReader br;
		try{
			br = new BufferedReader(new FileReader(file));
			numServs=Integer.parseInt(br.readLine());
			numCli=Integer.parseInt(br.readLine());
			tamBuff=Integer.parseInt(br.readLine());
			
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		Cliente[] clientes=new Cliente[numCli];
		Servidor[] servs=new Servidor[numServs];
		Buffer buff =Buffer.getBuffer();
		buff.setBuffSize(tamBuff);
		for( int i=0;i<servs.length;i++)
		{
			servs[i]=new Servidor(buff);
			servs[i].start();
		}
		for(int i=0;i<clientes.length;i++)
		{
			clientes[i]=new Cliente(buff, tamBuff, i);
			clientes[i].start();
		}
		
		
	}
}
