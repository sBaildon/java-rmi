import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.rmi.Naming;
import java.util.Random;

public class Requester implements Serializable {
	final static int UID = 32877315;
	final static int PASSCODE = 1340426414;
	final static String SERVER = "scc311-server.lancs.ac.uk";
	final static String SERVER_NAME = "CW_server";

	public static void main(String[] args) {
		CW_server_interface serv;
		Client_request request;
		Server_response response;
		File file = new File("E:/spec.doc");
		OutputStream doc = null;
		try {
			doc = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			serv = (CW_server_interface) Naming.lookup("rmi://" + SERVER + "/" + SERVER_NAME);
			request = GenerateRequest();
			response = serv.getSpec(UID, request);
			response.write_to(doc);
		} catch (Exception re) {
			System.out.println("RemoteException " + re);			
		} 
	}
	
	static int GenerateNonse() {
		Random rand = new Random();
		return rand.nextInt();
	}
	
	static Client_request GenerateRequest() {
		return new Client_request(UID, GenerateNonse(), PASSCODE);
	}

}
