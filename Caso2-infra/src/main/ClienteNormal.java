package main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.LinkedList;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class ClienteNormal {
	
	private static Socket s;
	private static BufferedReader bReader;
	private static PrintWriter pWriter;
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static final String[] ALGORITMOS = {"DES","AES","Blowfish","RC4","RSA","HMACMD5","HMACSHA1","HMACSHA256"};
	private static KeyPair keypair;
	private static String serverCert;
	private static String serverPublicKey;
	private static int numero1;
	private static int numero2;
	
	public static void conectarseS(int puerto) throws Exception{
		
		s = new Socket("localhost", puerto);
		bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pWriter = new PrintWriter(s.getOutputStream(),true);
		
	}
	
	public static void init() throws Exception {
		
		pWriter.println("HOLA");
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
		
	}
	
	public static void enviarAlgoritmos(LinkedList<Integer> pLista) throws IOException {
		
		String mes = "ALGORITMOS";
		for(int i : pLista) {
			mes = mes + ":" + ALGORITMOS[i-1];
		}
		System.out.println(mes);
		pWriter.println(mes);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
	}
	
	private static void crearllaves() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, new SecureRandom());
        keypair = keyGen.generateKeyPair();
	}
	
	public static void enviarCertificado() throws Exception {
		String certificadoEnString = "CERTCLIENT";
		pWriter.println(certificadoEnString);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
	}
	
	public static void recivirCertSer() throws Exception {
		serverCert = bReader.readLine();
		pWriter.println("OK");
	}
	
	public static void recivirkey() throws Exception {
		serverPublicKey = bReader.readLine();
	}
	
	public static void sendKey() throws Exception{
		String keyS = "LS";
		pWriter.println(keyS);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
	}
	
	public static void consultaConHMAC(String numC) throws Exception{
		String mess = numC + "";
		
	    String macB = numC + "";
	    
	    pWriter.println(mess);
	    pWriter.println(macB);
	}
	
	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Escriba el puerto del servidor: ");
		String p = reader.nextLine();
		int pu = Integer.parseInt(p);
		try {
			conectarseS(pu);
			init();
			LinkedList<Integer> algor = new LinkedList<Integer>();
			System.out.println("1)DES 2)AES 3)Blowfish 4)RC4");
			System.out.println("Escriba el numero del algoritmo SIMETRICO deseado");
			String alS = reader.nextLine();
			if(alS.equals("1")) {
				algor.add(1);
				numero1 = 1;
			}
			else if(alS.equals("2")) {
				algor.add(2);
				numero1 = 2;
			}
			else if(alS.equals("3")) {
				algor.add(3);
				numero1 = 3;
			}
			else if(alS.equals("4")) {
				algor.add(4);
				numero1 = 4;
			}
			System.out.println("Como algoritmo ASIMETRICO de usa RSA");
			algor.add(5);
			System.out.println("6)HMACMD5 7)HMACSHA1 8)HMACSHA256");
			System.out.println("Escriba el numero del algoritmo deseado");
			String al = reader.nextLine();
			if(al.equals("6")) {
				algor.add(6);
				numero2 = 6;
			}
			else if(al.equals("7")) {
				algor.add(7);
				numero2 = 7;
			}
			else if(al.equals("8")) {
				algor.add(8);
				numero2 = 8;
			}
			enviarAlgoritmos(algor);
			crearllaves();
			enviarCertificado();
			recivirCertSer();
			recivirkey();
			sendKey();
			System.out.println("Introdusca el numero de cuanta: ");
			String num = reader.nextLine();
			consultaConHMAC(num);
			String RespuestaF = bReader.readLine();
			System.out.println(RespuestaF);
		} catch (Exception e) {
			e.printStackTrace();
		}
		reader.close();
	}

}
