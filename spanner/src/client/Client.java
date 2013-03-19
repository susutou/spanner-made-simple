package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import paxos.PaxosAgent;

import network.Agent;
import network.Requests;

import twopc.TwoPCAgent;
import util.Utilities;

public class Client extends Thread {

	// Server side program
	public void run() {

		try {
			ServerSocket sSocket = new ServerSocket(Agent.port);

			TwoPCAgent tpcAgent = (TwoPCAgent) initializeRole();
			PaxosAgent paxosAgent = new PaxosAgent();
			
			Thread tpcThd = new Thread(tpcAgent);
			Thread paxosThd = new Thread(paxosAgent);
			
			tpcThd.start();
			paxosThd.start();
			
			while (Utilities.checkFlag()) {
				Socket socket = sSocket.accept();
				DataInputStream input = new DataInputStream(
						socket.getInputStream());
				String msg = input.readUTF();

				String role = "How can I know my role??";
				// if role="" than getLocalRole

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

		if ("2pc_prepare".equals(msg)) {

		} else if ("2pc_prepare_ack".equals(msg)) {

		} else if ("2pc_commit".equals(msg)) {

		} else if ("2pc_abort".equals(msg)) {

		} else if ("paxos_prepare".equals(msg)) {

		} else if ("paxos_prepare_ack".equals(msg)) {

		} else if (msg.startsWith("read") || msg.startsWith("write")) {

			agent.appendToMsgQueue(msg);

		} else {
			System.out.println("Unrecognized Msg: " + msg);
		}

	}

	// Initialize the role of this instance, get the role information from
	// outside
	public Agent initializeRole() {
		// initialze the role for this instance
		String serverName = Utilities.getServerName();

		Agent agent = new TwoPCAgent();

		return agent;
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.start();

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
				Requests.sendRequestTo(Agent.coordinator, Agent.port, msg);
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
