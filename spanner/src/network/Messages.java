package network;

import paxos.PaxosAgent;

public abstract class Messages {
	
	protected Agent sender;

    public Messages(Agent sender) {
        this.sender = sender;
    }

    
    
}
