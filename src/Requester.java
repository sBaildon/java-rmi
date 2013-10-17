import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.*;

public class Requester implements Serializable {
	final static int UID = 32877315;
	final static long PASSCODE = 1340426414;
	final static String SERVER = "scc311-server.lancs.ac.uk";
	final static String SERVER_NAME = "CW_server";
	final static String FILE_PATH = "files/spec.doc";
	final static String KEY_PATH = "files/32877315.key";

	public static void main(String[] args) {
		CW_server_interface serv;
		Client_request request;
		Server_response response;
		Scanner inputScanner;
		
		try {
			serv = (CW_server_interface) Naming.lookup("rmi://" + SERVER + "/" + SERVER_NAME);
			System.out.println("Connected to the server");
		} catch (Exception re) {
			System.out.println("Server connection error\n\n" + re);
			return;
		}	
		
		char input;
		inputScanner = new Scanner(System.in);
		
		do {
			System.out.print("Encrypt? (y/n) ");
			input = inputScanner.next().charAt(0);
		} while (input != 'y' && input != 'n');
		
		inputScanner.close();
		
		if (input == 'n') {
			request = generateRequest(false);
			
			try {
				response = serv.getSpec(UID, request);
			} catch (Exception ex) {
				System.out.println("Failed to fetch file\n\n" + ex);
				return;
			}
			
			writeFile(FILE_PATH, response);
		} else {
			request = generateRequest(true);
			
			try {
				Object key = readFile(KEY_PATH);
				
				Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
				SecretKey skey = (SecretKey) key;
				cipher.init(Cipher.ENCRYPT_MODE, skey);

				SealedObject obj = new SealedObject(request, cipher);
				obj = serv.getSpec(UID, obj);
				
				response = (Server_response) obj.getObject(skey);
			} catch (Exception ex) {
				System.out.println("Failed to fetch file\n\n" + ex);
				return;
			}
			
			writeFile(FILE_PATH, response);
		}
		
	}
	
	static int generateNonse() {
		Random rand = new Random(2000);
		return rand.nextInt();
	}
	
	/* True for encrypted, false for passcode auth */
	static Client_request generateRequest(boolean crypto) {
		if (!crypto) {
			return new Client_request(UID, generateNonse(), PASSCODE);
		} else {
			return new Client_request(UID, generateNonse());
		}
	}
	
	/* Open a file into an object */
	static Object readFile(String file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object obj = (Object) ois.readObject();
			ois.close();
			return obj;
		} catch (Exception e) {
			System.out.println("Failed reading key\n\n" + e);
		}
		return null;
	}
	
	static void writeFile(String filePath, Server_response resp) {
		File file;
		OutputStream stream;
		
		file = new File(filePath);
		try {
			stream = new FileOutputStream(file);
			resp.write_to(stream);
			stream.close();
		} catch (Exception ex) {
			System.out.println("Failed to write file\n\n");
		}
	}

}
