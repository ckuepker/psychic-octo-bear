package de.unioldenburg.jade.behaviours.registration;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Behaviour that can be used to allow the registration of agents on a specific
 * agent.
 * @author Christoph Küpker
 */
public class RegistrationServiceBehaviour extends WaitForMessageBehaviour {
    
    public static final int REGISTRATION_REQUEST_PERFORMATIVE = ACLMessage.REQUEST;
    public static final String REGISTRATION_REQUEST_CONTENT = "register";
    
    private Set<String> registeredLocalNames = new HashSet<String>();
    
    @Override
    public void handleMessage(ACLMessage m) {
        if (m.getPerformative() == REGISTRATION_REQUEST_PERFORMATIVE
                && m.getContent().equals(REGISTRATION_REQUEST_CONTENT)) {
            String localName = m.getSender().getLocalName();
            registeredLocalNames.add(localName);
        }
    } 
    
    public Collection<String> getRegisteredAgents() {
        return this.registeredLocalNames;
    }
}
