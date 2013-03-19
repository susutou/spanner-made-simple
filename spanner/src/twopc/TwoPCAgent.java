package twopc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import util.Utilities;

import network.Agent;

public class TwoPCAgent extends Agent implements Runnable {

	private Queue<Integer> operationsQueue = new ConcurrentLinkedQueue<Integer>();

	public void appendToMsgQueue(String operation) {
		operationsQueue.offer(Integer.parseInt(operation));
	}

	public void send2PCPrepare() {
		// do broadcast message
	}

	public void receive2PCPrepare() {
		// if paxos prepared, then
		send2PCACK();
	}

	public void send2PCACK() {
		
	}

	public void receive2PCACK() {
		
	}

	public boolean isAllPrepared() {

		return false;
	}

	public boolean prepare2PC(int operation) {

		send2PCPrepare();

		long startTime = System.currentTimeMillis() / 1000 + 10;
		while (Utilities.checkFlag()) {
			if (isAllPrepared())
				return true;
			if (System.currentTimeMillis() / 1000 > startTime) {
				return false;
			}
		}
		return false;
	}

	public void commit2PC() {

	}

	public void abort2PC() {
		// need to abort the transactions
	}

	public void run() {

		while (Utilities.checkFlag()) {
			if (!operationsQueue.isEmpty()) {
				if (prepare2PC(operationsQueue.poll())) {
					commit2PC();
				} else {
					abort2PC();
				}
			}
		}

	}

}
