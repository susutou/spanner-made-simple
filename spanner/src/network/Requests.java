package network;

import java.io.DataOutputStream;
import java.net.Socket;

public class Requests {

	public static void sendRequestTo(String ip,  int port, String msg) throws Exception{
		Socket socket = new Socket(ip, port);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(msg);
		socket.close();
    }
	
	
	
	
	
}
