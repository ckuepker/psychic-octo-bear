package de.unioldenburg.jade.contractnet.messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.Set;

/**
 * Message used to inform all participants about all participants.
 * @author Christoph Küpker
 */
public class ParticipantListMessage extends ACLMessage {
    
    public ParticipantListMessage(Set<String> participantList) {
        setPerformative(INFORM);
        System.out.println("Preparing participant list broadcast");
        setContent("participants:"+participantList.toString());
        for (String participant : participantList) {
            this.addReceiver(new AID(participant, AID.ISLOCALNAME));
        }
    }
}
