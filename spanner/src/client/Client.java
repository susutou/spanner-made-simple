package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import network.Agents;

import twopc.TwoPCAgent;
import util.Utilities;

public class Client extends Thread {

	// Server side program
	public void run() {

		try {
			ServerSocket sSocket = new ServerSocket(Agents.port);

			TwoPCAgent tpcAgent = (TwoPCAgent) initializeRole();
			//PaxosAgent paxosAgent = new PaxosAgent();

			Thread tpcThd = new Thread(tpcAgent);
			//Thread paxosThd = new Thread(paxosAgent);

			tpcThd.start();
			//paxosThd.start();

			while (Utilities.checkFlag()) {
				Socket socket = sSocket.accept();
				DataInputStream input = new DataInputStream(
						socket.getInputStream());
				String msg = input.readUTF();
				
				System.out.println("<<<--Get MSG---<<<: " + msg);

				// pass the command to the right role
				handle2PCMessage(msg, tpcAgent);

				socket.close();
			}

			sSocket.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handle2PCMessage(String msg, TwoPCAgent agent) {

		if (msg.startsWith("2pc_prepare")) {
			agent.receive2PCPrepare(msg);
			
		} else if (msg.startsWith("2pc_prepare_ack")) {
			agent.receive2PCACK(msg);
			
		} else if (msg.startsWith("2pc_commit")) {
			agent.commit2PC(msg);
			
		} else if (msg.startsWith("2pc_abort")) {
			agent.abort2PC(msg);
			
		} else if (msg.startsWith("paxos_ready")) {
			agent.send2PCACK(msg);
			
		} else if (msg.startsWith("paxos_fail")) {
			agent.setPaxosFail();
			
		} else if (msg.startsWith("read") || msg.startsWith("write")
				|| "begin".equals(msg) || "commit".equals(msg)
				|| "abort".equals(msg)) {

			agent.appendToMsgQueue(msg);

		} else {
			System.out.println("Unrecognized Command: " + msg);
		}

	}

	// Initialize the role of this instance, get the role information from
	// outside
	public Agents initializeRole() {
		// initialze the role for this instance
		String serverName = Utilities.getServerName();

		Agents agent = new TwoPCAgent();

		return agent;
	}

	public static void main(String[] args) {

		// Initialize the basic instance information
		Agents initilization = new Agents();

		Client client = new Client();
		//client.start();

		// Run client to send request, format: peer c ip port order
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		try {

			while (true) {
				System.out.println("Give me some test message");
				String msg = stdIn.readLine().trim();

				if ("exit".equals(msg))
					break;
				
				System.out.println(msg);
				//Requests.sendRequestTo(Agents.coordinator, Agents.port, msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			stdIn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
}
