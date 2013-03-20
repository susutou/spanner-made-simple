package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import log.logger;

import network.Agents;
import network.MessageHelper;
import network.Requests;

import twopc.TwoPCAgent;
import util.Utilities;

public class Client extends Thread {

	// Server side program
	public void run() {
		try {
			ServerSocket sSocket = new ServerSocket(Agents.port);

			TwoPCAgent tpcAgent = new TwoPCAgent();
			// PaxosAgent paxosAgent = new PaxosAgent();

			Thread tpcThd = new Thread(tpcAgent);
			// Thread paxosThd = new Thread(paxosAgent);

			tpcThd.start();
			// paxosThd.start();
			
			while (Utilities.checkFlag()) {
				Socket socket = sSocket.accept();
				DataInputStream input = new DataInputStream(
						socket.getInputStream());
				String msg = input.readUTF();
				
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
			logger.log("<<<--Get 2pc_prepare Msg--<<<: " + msg+"\n");
			agent.receive2PCPrepare(msg);

		} else if (msg.startsWith("2pc_prepare_ack")) {
			logger.log("<<<--Get 2pc_prepare_ack Msg--<<<: " + msg+"\n");
			agent.receive2PCACK(msg);

		} else if (msg.startsWith("2pc_commit")) {
			logger.log("<<<--Get 2pc_commit Msg--<<<: " + msg+"\n");
			agent.commit2PC(msg);

		} else if (msg.startsWith("2pc_abort")) {
			logger.log("<<<--Get 2pc_abort Msg--<<<: " + msg+"\n");
			agent.abort2PC(msg);

		} else if (msg.startsWith("paxos_ready")) {
			logger.log("<<<--Get paxos_ready Msg--<<<: " + msg+"\n");
			agent.send2PCACK(msg);

		} else if (msg.startsWith("paxos_fail")) {
			logger.log("<<<--Get paxos_fail Msg--<<<: " + msg+"\n");
			agent.setPaxosFail();

		} else if (msg.startsWith("read") || msg.startsWith("write")
				|| msg.startsWith("begin") || msg.startsWith("commit")
				|| msg.startsWith("abort")) {
			
			logger.log("<<<--Get Operation Msg--<<<: " + msg+"\n");
			agent.appendToMsgQueue(msg);
		} else {
			System.out.println("Unrecognized Command: " + msg+"\n");
		}

	}

	public static void main(String[] args) {

		// Initialize the basic instance information
		Agents initilization = new Agents();

		Client client = new Client();
		client.start();

		// Run client to send request, format: peer c ip port order
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			int opCounter = 1;
			while (true) {
				System.out.println("Please type in transactions:");
				System.out
						.println("The command should be in this syntax: "
								+ "[TxnID]@[operations],[keys],[value]. For example, 1@read,column1");
				
				String msg = stdIn.readLine().trim();
				if ("exit".equals(msg))
					break;
				// two values in the params, first one is the txnID and the
				// second one the operation.
				String[] params = MessageHelper.parseInputCommand(msg);
				// operations#txnID#opID
				msg = params[1] + "#" + params[0] + "#" + opCounter++;
				Requests.sendRequestTo(Agents.coordinator, Agents.port, msg);
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
