package paxos;

import network.Agent;

public abstract class Messages {
	
	protected Agent sender;

    public Messages(Agent sender) {
        this.sender = sender;
    }

    
    
}
