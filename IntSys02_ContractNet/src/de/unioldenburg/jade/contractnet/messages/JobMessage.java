package de.unioldenburg.jade.contractnet.messages;

import de.unioldenburg.jade.contractnet.agents.Participant;
import de.unioldenburg.jade.contractnet.data.Job;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.Set;

/**
 * Message for offering a Job and requesting proposals (biddings) in an auction.
 * Job id is stored in user set parameter of the message for later referal.
 * @author Christoph Küpker
 */
public class JobMessage extends ACLMessage {
    public static final String CONTENT = "job";
    
    public JobMessage(Job job, Set<String> bidders) {
        setPerformative(ACLMessage.REQUEST);
        setContent(CONTENT);
        addUserDefinedParameter(Participant.KEY_ID, job.getId());
        addUserDefinedParameter(Participant.KEY_START, ""+job.getStarttime());
        addUserDefinedParameter(Participant.KEY_END, ""+job.getEndtime());
        for (String b : bidders) {
            addReceiver(new AID(b, AID.ISLOCALNAME));
        }
    }
}
