import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.Random;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Requester implements Serializable {
	final static int UID = 32877315;
	final static long PASSCODE = 1340426414;
	final static String SERVER = "scc311-server.lancs.ac.uk";
	final static String SERVER_NAME = "CW_server";

	public static void main(String[] args) {
		CW_server_interface serv;
		Client_request request;
		Server_response response;
		OutputStream stream;
		File spec;
		
		try {
			serv = (CW_server_interface) Naming.lookup("rmi://" + SERVER + "/" + SERVER_NAME);
			request = generateRequest(false);
			response = serv.getSpec(UID, request);

			if (isWindows()) {				
				spec = new File(System.getenv("USERPROFILE") + "/spec.doc");
			} else {
				spec = new File(System.getenv("HOME") + "/spec.doc");
			}
			
			stream = new FileOutputStream(spec);
			response.write_to(stream);
			stream.close();
		} catch (Exception re) {
			System.out.println("RemoteException " + re);			
		} 
	}
	
	static int generateNonse() {
		Random rand = new Random();
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
	
	/* Check for Windows OS, assume UNIX if not */
	static boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();		
		return (OS.indexOf("win") >= 0);
	}

}
