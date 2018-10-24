package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	
	private static Socket s;
	private static BufferedReader bReader;
	private static PrintWriter pWriter;
	
	public static void conectarseS(int puerto) throws Exception{
		
		s = new Socket("localhost", puerto);
		bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pWriter = new PrintWriter(s.getOutputStream(),true);
		
	}
	
	public static boolean init() throws Exception {
		
		pWriter.println("HOLA");
		String re = bReader.readLine();
		return re.equals("OK");
		
	}

	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Escriba el puerto del servidor: ");
		String p = reader.nextLine();
		int pu = Integer.parseInt(p);
		try {
			conectarseS(pu);
			boolean conex = init();
			if(conex) {
				System.out.println("Si conecta");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
