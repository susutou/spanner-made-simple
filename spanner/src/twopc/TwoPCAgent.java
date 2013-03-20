package twopc;

import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import log.logger;
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
		//operations#txnID#opID
		operationsQueue.offer(operation);
	}

	public boolean prepare2PC(String operation) {
		//operation = operations#txnID#opID
		
		//send to cohorts the prepare message.
		send2PCPrepare(operation);
		
		String opID = MessageHelper.getOpIDFromOperation(operation);
		String ops = MessageHelper.getOpsFromOperation(operation);
		//set for time-out
		long startTime = System.currentTimeMillis() / 1000 + 3;
		
		while (Utilities.checkFlag()) {
			
			//check whether all cohorts are ready
			if (isAllPrepared(opID,ops))
				return true;
			//10s time out
			if (System.currentTimeMillis() / 1000 > startTime) {
				return false;
			}
		}
		return false;
	}
	
	public void send2PCPrepare(String msg){
		//msg = operations#txnID#opID
		String opID = MessageHelper.getOpIDFromOperation(msg);
		receivedAck.put(opID, 0);
		String newMsg = "2pc_prepare#"+msg;
		try {
			logger.log(">>>--Send 2PC-Prepare MSG To 2PC Cohort--->>>: " + newMsg+"\n");
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(), newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//2PC cohorts receive 2pc_prepare message
	public void receive2PCPrepare(String msg) {
		//msg = 2pc_prepare#operations#txnID#opID
		String newMsg = "paxos_prepare#"+MessageHelper.removeHeaderFromMsg(msg);
		
		try {
			logger.log(">>>--Send 2PC-Prepare MSG To Paxos Leader--->>>: " + newMsg+"\n");
			System.out.println(">>>--Send 2PC-Prepare MSG To Paxos Leader--->>>: " + newMsg+"\n");
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), newMsg);
			
			Requests.sendRequestTo2PC(Utilities.getServerName(), MessageHelper.removeHeaderFromMsg(msg));
		
		} catch (Exception e) {
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
		//msg = paxos_ready#operations#txnID#opID
		String newMsg = "2pc_prepare_ack#" + MessageHelper.removeHeaderFromMsg(msg);
		try {
			Requests.sendRequestTo(Agents.coordinator, Agents.port, newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receive2PCACK(String msg) {
		//msg = 2pc_prepare_ack#operations#txnID#opID
		String opID = MessageHelper.getOpIDFromHeaderMsg(msg);  
		
		receivedAck.put(opID,receivedAck.get(opID)+1);
	}

	public boolean isAllPrepared(String opID, String ops) {
		
		if(paxosFailure) {
			paxosFailure = false;
			return false;
		}
		
		if(tpcFailure) {
			tpcFailure = false;
			return false;
		}
		
		
		if( !ops.contains("X") && !ops.contains("Y") ){
			if( receivedAck.get(opID) >= Agents.get2PCCohortSize())
				return true;
			}
		else{
			if( receivedAck.get(opID) >= 1)
				return true;
		}
				
		return false;
	}

	public void commit2PC(String msg) {
		//msg = 2pc_commit#operations#txnID#opID
		String newMsg = "paxos_commit#"+MessageHelper.removeHeaderFromMsg(msg);
		logger.log(">>>--Send 2PC-Commit MSG To Paxos Leader--->>>: " + newMsg+"\n");
		logger.log("<<<--Operation Committed!--->>>: " + newMsg+"\n");
		try {
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void abort2PC(String msg) {
		//msg = 2pc_commit#operations#txnID#opID
		String newMsg = "paxos_abort#"+MessageHelper.removeHeaderFromMsg(msg);
		logger.log(">>>--Send 2PC-Abort MSG To Paxos Leader--->>>: " + newMsg+"\n");
		logger.log("<<<--Operation Aborted!--->>>: " + newMsg+"\n");
		try {
			Requests.sendRequestToPaxosLeader(Utilities.getServerName(), newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendCommit2PC(String msg){
		//msg = operations#txnID#opID
		String newMsg = "2pc_commit#"+msg;
		try {
			logger.log(">>>--Send 2PC-Commit MSG To 2PC Cohort--->>>: " + newMsg+"\n");
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(),newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendAbort2PC(String msg){
		//msg = operations#txnID#opID
		String newMsg = "2pc_abort#"+msg;
		try {
			logger.log(">>>--Send 2PC-Abort MSG To 2PC Cohort--->>>: " + newMsg+"\n");
			Requests.sendRequestTo2PCCohort(Utilities.getServerName(),newMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while (Utilities.checkFlag()) {
			if (!operationsQueue.isEmpty()) {
				//msg = operations#txnID#opID
				String msg = operationsQueue.poll();
				System.out.println("******Get a Msg From the Queue******: "+msg+"\n");
				if (prepare2PC(msg)) {
					sendCommit2PC(msg);
				} else {
					sendAbort2PC(msg);
				}
			}
		}

	}
	
}
