package network;

import java.io.DataOutputStream;
import java.net.Socket;

public class Requests {

	public static void sendRequestTo(String ip, int port, String msg)
			throws Exception {
		Socket socket = new Socket(ip, port);
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos.writeUTF(msg);
		socket.close();
	}

	public static void sendRequestTo2PC(String sender, String msg) throws Exception{
		if(sender.contains("X")){
			sendRequestTo(Agents.tpc_corhort[0], Agents.port, "paxos_ready#"+msg);
		}else if(sender.contains("Y")){
			sendRequestTo(Agents.tpc_corhort[1], Agents.port, "paxos_ready#"+msg);
		}
	}
	
	public static void sendRequestToPaxosLeader(String sender, int port,
			String msg) throws Exception {
		if (sender.contains("X")){
			sendRequestTo(Agents.paxos_leaders[0], Agents.paxosPort, sender
					+ "#" + msg);
			//System.out.println(sender+"#"+msg);
		}
		else if (sender.contains("Y")){
			sendRequestTo(Agents.paxos_leaders[1], Agents.paxosPort, sender
					+ "#" + msg);
			//System.out.println(sender+"#"+msg);	
		}
		else {
			System.out.println("Server Name Error");
		}
	}

	public static void sendRequestToPaxosLeader(String sender, String msg)
			throws Exception {
		sendRequestToPaxosLeader(sender, Agents.paxosPort, msg);
	}

	public static void sendRequestTo2PCCohort(String sender, String msg)
			throws Exception {
		for (String hostname : Agents.tpc_corhort) {
			sendRequestTo(hostname, Agents.port, msg);
		}
	}

	public static void sendRequestToServer(String serverName, String msg)
			throws Exception {
		sendRequestTo(Agents.getIP(serverName), Agents.port, msg);
	}

}
