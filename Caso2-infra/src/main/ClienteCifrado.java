package main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

public class ClienteCifrado {
	
	//private static Socket s;
	//private static BufferedReader bReader;
	//private static PrintWriter pWriter;
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	private static final String[] ALGORITMOS = {"DES","AES","Blowfish","RC4","RSA","HMACMD5","HMACSHA1","HMACSHA256"};
	private static KeyPair keypair;
	private static X509Certificate serverCert;
	private static SecretKey serverPublicKey;
	private static int numero1;
	private static int numero2;
	
	public ClienteCifrado(){
		super();
	}
	
	private static void crearllaves() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024, new SecureRandom());
        keypair = keyGen.generateKeyPair();
	}
	
	public static void conectarseS(String ip, int puerto, Socket s,BufferedReader bReader,PrintWriter pWriter) throws Exception{
		
		s = new Socket(ip, puerto);
		bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		pWriter = new PrintWriter(s.getOutputStream(),true);
		
	}
	
	public static void init(Socket s,BufferedReader bReader,PrintWriter pWriter) throws Exception {
		
		
		pWriter.println("HOLA");
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re+" (init)");
		
	}
	
	public static void enviarAlgoritmos(LinkedList<Integer> pLista,Socket s,BufferedReader bReader,PrintWriter pWriter) throws IOException {
		
		String mes = "ALGORITMOS";
		for(int i : pLista) {
			mes = mes + ":" + ALGORITMOS[i-1];
		}
		System.out.println(mes);
		pWriter.println(mes);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re+" (envialAlgo)");
	}
	
	public static String printByteArrayHexa(byte[] byteArray) {
		String out = "";
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] & 0xff) <= 0xf) {
				out += "0";
			}
			out += Integer.toHexString(byteArray[i] & 0xff).toUpperCase();
		}

		return out;
	}
	
	public static X509Certificate generarCertificado() throws Exception{
		
		Provider bcProvider = new BouncyCastleProvider();
	    Security.addProvider(bcProvider);
		
		Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.DAY_OF_YEAR, 2);
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = expiry.getTime();
        BigInteger serialNum = BigInteger.valueOf(System.currentTimeMillis());
        X500Name dnName = new X500Name("cn=example");
        
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256WithRSA").build(keypair.getPrivate());
        
        JcaX509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(dnName, serialNum, startDate, endDate, dnName, keypair.getPublic());
        
        return new JcaX509CertificateConverter().setProvider(bcProvider).getCertificate(certBuilder.build(contentSigner));
		
	}
	
	public static void enviarCertificado(BufferedReader bReader,PrintWriter pWriter) throws Exception {
		X509Certificate certificado = generarCertificado();
		byte[] certificadoEnBytes = certificado.getEncoded( );
		String certificadoEnString = printByteArrayHexa(certificadoEnBytes);
		pWriter.println(certificadoEnString);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re+" (enviarCert)");
	}
	
	public static void recivirCertSer(BufferedReader bReader,PrintWriter pWriter) throws Exception {
		String servercert = bReader.readLine();
		//System.out.println(servercert);
		byte[] RAWcert = new byte[1000];
		RAWcert = hexStringToByteArray(servercert);
		//System.out.println("copia el certificado el byte");
		serverCert = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(RAWcert));
		//System.out.println("le da valor al serverCent");
		pWriter.println("OK");
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static void recivirkey(BufferedReader bReader,PrintWriter pWriter) throws Exception {
		String serverKeys = bReader.readLine();
		//System.out.println(serverKeys);
		byte[] RAWserverKey = hexStringToByteArray(serverKeys);
		Cipher cipher = Cipher.getInstance(ALGORITMOS[4]); 
		cipher.init(Cipher.DECRYPT_MODE, keypair.getPrivate());
		byte[] serverKey = cipher.doFinal(RAWserverKey);
		serverPublicKey = new SecretKeySpec(serverKey,0,serverKey.length,ALGORITMOS[numero1 - 1]);
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static void sendKey(BufferedReader bReader,PrintWriter pWriter) throws Exception{
		byte[] key = serverPublicKey.getEncoded();
		Cipher cipher = Cipher.getInstance(ALGORITMOS[4]);
		cipher.init(Cipher.ENCRYPT_MODE, serverCert.getPublicKey());
		byte[] encryptedKey = cipher.doFinal(key);
		String keyS = bytesToHex(encryptedKey);
		pWriter.println(keyS);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re+" (sendKey)");
	}
	
	public static void consultaConHMAC(String numC,BufferedReader bReader,PrintWriter pWriter) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITMOS[numero1 - 1]);
		cipher.init(Cipher.ENCRYPT_MODE, serverPublicKey);
		byte[] mess = cipher.doFinal(numC.getBytes());
		
	    Mac mac = Mac.getInstance(ALGORITMOS[numero2 - 1]);
	    mac.init(serverPublicKey);
	    byte[] macB = mac.doFinal(numC.getBytes());
	    
	    pWriter.println(DatatypeConverter.printHexBinary(mess));
	    pWriter.println(DatatypeConverter.printHexBinary(macB));
	}

	public void run() {
		
		Socket s;
		BufferedReader bReader;
		PrintWriter pWriter;
		
		//PrintWriter textWriter;
		
		Scanner reader = new Scanner(System.in);
		System.out.println("Escriba el puerto del servidor: ");
		//String p = reader.nextLine();
		//int pu = Integer.parseInt(p);
		int pu = 5000;
		try {
			System.out.println("Escriba la ip del servidor: ");
			//String ip = reader.nextLine();
			String ip = "157.253.202.18";
			
			//conectarseS(ip,pu,s,bReader,pWriter);
			s = new Socket(ip, pu);
			bReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pWriter = new PrintWriter(s.getOutputStream(),true);
			
			//textWriter = new PrintWriter("tiempos.txt");
			
			init(s,bReader,pWriter);
			LinkedList<Integer> algor = new LinkedList<Integer>();
			System.out.println("1)DES 2)AES 3)Blowfish 4)RC4");
			System.out.println("Escriba el numero del algoritmo SIMETRICO deseado");
			//String alS = reader.nextLine();
			String alS = "2";
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
			//String al = reader.nextLine();
			String al = "8";
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
			enviarAlgoritmos(algor,s,bReader,pWriter);
			crearllaves();
			enviarCertificado(bReader,pWriter);
			recivirCertSer(bReader,pWriter);
			recivirkey(bReader,pWriter);
			sendKey(bReader,pWriter);
			System.out.println("Introdusca el numero de cuanta: ");
			//String num = reader.nextLine();
			String num = "555";
			long startTime = System.nanoTime();
			consultaConHMAC(num,bReader,pWriter);
			String RespuestaF = bReader.readLine();
			System.out.println(RespuestaF);
			long estimatedTime = System.nanoTime() - startTime;
			double re = (double)estimatedTime / 1_000_000_000.0;
			System.out.println(re + " Segundos");
			//textWriter.println(re+"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		reader.close();
	}

}
