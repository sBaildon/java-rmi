import java.io.Serializable;
import java.rmi.Naming;
import java.util.Random;

public class Requester implements Serializable {
	final static int UID = 32877315;
	final static int PASSCODE = 1340426414;
	final static String SERVER = "scc311-server.lancs.ac.uk/";
	final static String SERVER_NAME = "CW_server";

	public static void main(String[] args) {
		Client_request request;
		CW_server_interface serv;
		Server_response doc;
		
		try {
			serv = (CW_server_interface) Naming.lookup("rmi://" + SERVER + SERVER_NAME);
			request = GenerateRequest();
			doc = serv.getSpec(UID, request);
		} catch (Exception re) {
			System.out.println("RemoteException"+re);			
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
