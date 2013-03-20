package twopc;

import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import util.Utilities;
import network.Agents;
import network.MessageHelper;
import network.Requests;

public class TwoPCAgent extends Agents implements Runnable {

	private Queue<String> operationsQueue = new ConcurrentLinkedQueue<String>();
	private boolean paxosFailure = false;
	private boolean tpcFailure = false;
	private Map<String, Integer> receivedAck = new Hashtable<String,Integer>();
	
	
	public void appendToMsgQueue(String operation) {
		operationsQueue.offer(operation);
	}

	
	public boolean prepare2PC(String operation) {
		
		//send to cohorts the prepare message.
		send2PCPrepare(operation);
		
		String msgID = "";
		
		//set for time-out
		long startTime = System.currentTimeMillis() / 1000 + 10;
		
		while (Utilities.checkFlag()) {
			
			//check whether all cohorts are ready
			if (isAllPrepared(msgID))
				return true;
			//10s time out
			if (System.currentTimeMillis() / 1000 > startTime) {
				return false;
			}
		}
		return false;
	}
	
	public void send2PCPrepare(String msg){
		
		String msgID = "";
		receivedAck.put(msgID, 0);
		
		try {
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(), "2pc_prepare");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	//2PC cohorts receive 2pc_prepare message
	public void receive2PCPrepare(String msg) {
		
		String operation = MessageHelper.parseMsg(msg);
		String txnID = ""; 		
				
		try {
			System.out.println(">>>--Send 2PC-Prepare MSG To Paxos Leader--->>>: " + msg);
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), "2pc_prepate"+"#"+txnID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void setPaxosFail(){
		paxosFailure = true;
	}
	
	public void set2PCFailure(){
		tpcFailure = false;
	}
	
	public void send2PCACK(String msg) {
		try {
			String txnID="";
			Requests.sendRequestTo(Agents.coordinator, Agents.port, "2pc_prepare_ack"+"#"+txnID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receive2PCACK(String msg) {
		
		String msgID = "";  
		
		receivedAck.put(msgID,receivedAck.get(msgID)+1);
	}

	public boolean isAllPrepared(String msgID) {
		if(paxosFailure) {
			paxosFailure = false;
			return false;
		}
		
		if(tpcFailure) {
			tpcFailure = false;
			return false;
		}
		return false;
	}

	public void commit2PC(String msg) {
		String txnID = "";
		System.out.println(">>>--Send 2PC-Commit MSG To Paxos Leader--->>>: " + msg);
		try {
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), "2pc_commit"+"#"+txnID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void abort2PC(String msg) {
		String txnID = "";
		System.out.println(">>>--Send 2PC-Abort MSG To Paxos Leader--->>>: " + msg);
		try {
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), "2pc_abort"+"#"+txnID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendCommit2PC(String msg){
		String txnID = "";
		
		try {
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(),"2pc_commit"+"#"+txnID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendAbort2PC(String msg){
		String txnID = "";
		try {
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(),"2pc_abort"+"#"+txnID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void run() {

		while (Utilities.checkFlag()) {
			if (!operationsQueue.isEmpty()) {
				String msg = operationsQueue.poll();
				if (prepare2PC(msg)) {
					sendCommit2PC(msg);
				} else {
					sendAbort2PC(msg);
				}
			}
		}

	}
	
}
