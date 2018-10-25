package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.*;

public class Cliente {
	
	private static Socket s;
	private static BufferedReader bReader;
	private static PrintWriter pWriter;
	private static final String[] ALGORITMOS = {"AES","Blowfish","RSA","HMACMD5","HMACSHA1","HMACSHA256"};
	private static KeyPair keypair;
	private static PrivateKey privKey;
	private static PublicKey pubKey;
	
	private static void crearllaves() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        keypair = keyGen.generateKeyPair();
        privKey = keypair.getPrivate();
        pubKey = keypair.getPublic();
	}
	
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
		pWriter.println(mes);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
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
		
		Calendar expiry = Calendar.getInstance();
        expiry.add(Calendar.DAY_OF_YEAR, 2);
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = expiry.getTime();
        BigInteger serialNum = BigInteger.valueOf(System.currentTimeMillis());
        //X509Certificate caCert = (X509Certificate) pubKey;
        
        X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
        X500Principal              subjectName = new X500Principal("CN=Certificate");
        certGen.setSerialNumber(serialNum);
        //certGen.setIssuerDN(caCert.getSubjectX500Principal());
        certGen.setNotBefore(startDate);
        certGen.setNotAfter(endDate);
        certGen.setSubjectDN(subjectName);
        certGen.setPublicKey(pubKey);
        // TODO no funciona porque no c que poner en SignatureAlgorithm
        certGen.setSignatureAlgorithm("sha1RSA");
        
        X509Certificate cert = certGen.generate(privKey, "BC");
        return cert;
		
	}
	
	public static void enviarCertificado() throws Exception {
		X509Certificate certificado = generarCertificado();
		byte[] certificadoEnBytes = certificado.getEncoded( );
		String certificadoEnString = printByteArrayHexa(certificadoEnBytes);
		pWriter.println(certificadoEnString);
		String re = bReader.readLine();
		System.out.println("SERVIDOR: "+re);
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
			boolean ya = false;
			while(!ya) {
				System.out.println("1)AES 2)Blowfish 3)RSA 4)HMACMD5 5)HMACSHA1 6)HMACSHA256");
				System.out.println("Escriba el numero del algoritmo deseado o 'ya' para continuar");
				System.out.println("Algoritmos inscritos: " + algor.size());
				String al = reader.nextLine();
				if(al.equals("ya")) {
					ya = true;
				}
				else if(al.equals("1")) {
					algor.add(1);
				}
				else if(al.equals("2")) {
					algor.add(2);
				}
				else if(al.equals("3")) {
					algor.add(3);
				}
				else if(al.equals("4")) {
					algor.add(4);
				}
				else if(al.equals("5")) {
					algor.add(5);
				}
				else if(al.equals("6")) {
					algor.add(6);
				}
			}
			enviarAlgoritmos(algor);
			crearllaves();
			enviarCertificado();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
