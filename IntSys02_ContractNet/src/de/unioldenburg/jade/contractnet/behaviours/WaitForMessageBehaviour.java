package de.unioldenburg.jade.contractnet.behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

/**
 * Base class for convenience handling of incoming messages. Subclasses must 
 * implement handleMessage() to define how messages will be treated.
 * @author Christoph Küpker
 */
public abstract class WaitForMessageBehaviour extends Behaviour {
    
    private boolean done;

    @Override
    public void action() {
        ACLMessage msg;
        while (null != (msg = getAgent().receive())) {
            this.handleMessage(msg);
        }
    }

    @Override
    public boolean done() {
        return done;
    }
    
    public abstract void handleMessage(ACLMessage m);
    
    protected void setDone(boolean done) {
        this.done = done;
    }
    
}
