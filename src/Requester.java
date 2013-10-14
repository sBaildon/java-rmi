import java.io.Serializable;
import java.rmi.Naming;
import java.util.Random;

public class Requester implements Serializable {
	final static int uid = 32877315;
	final static int passcode = 1340426414;

	public static void main(String[] args) {
		Client_request request;
		CW_server_interface serv;
		Server_response doc;
		
		try {
			serv = (CW_server_interface) Naming.lookup("rmi://scc311-server.lancs.ac.uk/CW_server");
			request = GenerateRequest();
			doc = serv.getSpec(uid, request);
		} catch (Exception re) {
			System.out.println("RemoteException"+re);			
		} 
	}
	
	static int GenerateNonse() {
		Random rand = new Random();
		return rand.nextInt();
	}
	
	static Client_request GenerateRequest() {
		return new Client_request(uid, GenerateNonse(), passcode);
	}

}
