package srvcifIC201820;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.Security;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class Coordinador {

	private static ServerSocket ss;	
	private static final String MAESTRO = "MAESTRO: ";
	static java.security.cert.X509Certificate certSer; /* acceso default */
	static KeyPair keyPairServidor; /* acceso default */

	/**
	 * @param args
	 */
	
	public static double getSystemCpuLoad() throws Exception 
	{
		 MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		 ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
		 AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
		 if (list.isEmpty()) return Double.NaN;
		 Attribute att = (Attribute)list.get(0);
		 Double value = (Double)att.getValue();
		 // usually takes a couple of seconds before we get real values
		 if (value == -1.0) return Double.NaN;
		 // returns a percentage value with 1 decimal point precision
		 return ((int)(value * 1000) / 10.0);
		 }
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub

		//System.out.println(MAESTRO + "Establezca puerto de conexion:");
		//InputStreamReader isr = new InputStreamReader(System.in);
		//BufferedReader br = new BufferedReader(isr);
		//int ip = Integer.parseInt(br.readLine());
		System.out.println(MAESTRO + "Empezando servidor maestro en puerto " + 5000);
		// Adiciona la libreria como un proveedor de seguridad.
		// Necesario para crear certificados.
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());		
		keyPairServidor = Seg.grsa();
		certSer = Seg.gc(keyPairServidor);

		//Crea el pool
		int numT = 2;
		ExecutorService exec = Executors.newFixedThreadPool(numT);
		System.out.println(MAESTRO + "Creado pool de tamanio "+ numT);
		int idThread = 0;
		// Crea el socket que escucha en el puerto seleccionado.
		ss = new ServerSocket(5000);
		System.out.println(MAESTRO + "Socket creado.");
		while (true) {
System.out.println(getSystemCpuLoad());	
try { 
				Socket sc = ss.accept();
				System.out.println(MAESTRO + "Cliente " + idThread + " aceptado.");
				//Delegado3 d3 = new Delegado3(sc,idThread);
				exec.execute(new Delegado3(sc,idThread));
				idThread++;
				//d3.start();
			} 
			catch (IOException e) 
			{
				System.out.println(MAESTRO + "Error creando el socket cliente.");
				e.printStackTrace();
			}
			

		}

	}
}



	
