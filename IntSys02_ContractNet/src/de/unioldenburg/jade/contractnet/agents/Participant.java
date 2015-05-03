package de.unioldenburg.jade.contractnet.agents;

import de.unioldenburg.jade.contractnet.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.contractnet.data.Job;
import de.unioldenburg.jade.contractnet.messages.JobMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class Participant extends Agent {

    private final static String BIDDING_CONTENT = "bidding";
    private final static String ACCEPT_CONTENT = "accepted";
    private final static String KEY_BIDDING = "bidding";
    public static final String KEY_ID = "job_id";
    public static final String KEY_START = "starttime";
    public static final String KEY_END = "endtime";

    /**
     *
     * @author Christoph Küpker
     */
    private class BidderBehaviour extends WaitForMessageBehaviour {

        @Override
        public void handleMessage(ACLMessage m) {
            if (m.getPerformative() == ACLMessage.REQUEST && m.getContent().equals(JobMessage.CONTENT)) {
                System.out.println(getLocalName() + ": Received job offer from auctioneer");
                Job job = Job.parseJobFromMessage(m);
                Random r = new Random();
                int bidding = r.nextInt(job.getEndtime() - job.getStarttime()) + job.getStarttime();
                ACLMessage reply = m.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(BIDDING_CONTENT);
                reply.addUserDefinedParameter(KEY_ID, job.getId());
                reply.addUserDefinedParameter(KEY_BIDDING, "" + bidding);
                System.out.println(getLocalName() + ": Sending offer " + bidding
                        + " for " + job.toString());
                send(reply);
            } else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL && m.getContent().equals(ACCEPT_CONTENT)) {
                System.out.println(getLocalName()+": My proposal was accepted. I will now act as auctioneer");
                role = ParticipantRole.AUCTIONEER;
                addBehaviour(new AuctioneerBehaviour());
                setDone(true);
            }
        }
    }

    /**
     *
     * @author Christoph Küpker
     */
    private class AuctioneerBehaviour extends Behaviour {

        private AuctionState state = AuctionState.IDLE;
        private Job auctionedJob = null;
        private boolean done = false;
        private int biddings = 0;
        private ACLMessage highestBiddingMessage = null;
        private int highestBidding = -1;

        @Override
        public void action() {
            ACLMessage message = receive();
            if (message != null) {
                if (message.getPerformative() == ACLMessage.PROPOSE && message.getContent().equals(BIDDING_CONTENT)) {
                    String id = message.getUserDefinedParameter(KEY_ID);
                    if (auctionedJob.getId().equals(id)) {
                        System.out.println(getLocalName()+": Bidding received");
                        biddings++;
                        int bidding = Integer.parseInt(message.getUserDefinedParameter(KEY_BIDDING));
                        if (highestBidding < 0 || bidding < highestBidding) {
                            highestBidding = bidding;
                            highestBiddingMessage = message;
                            System.out.println(getLocalName()+": New highest bidding "
                                    +highestBidding+" received from "
                                    +highestBiddingMessage.getSender().getLocalName());
                        }
                        if (biddings == otherParticipants.size()) {
                            System.out.println(getLocalName()+": All biddings received. Closing auction");
                            // Close auction
                            ACLMessage proposalAccept = highestBiddingMessage.createReply();
                            proposalAccept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            proposalAccept.setContent(ACCEPT_CONTENT);
                            send(proposalAccept);
                            System.out.println(getLocalName()+": Proposal acceptance sent to new auctioneer. Demoting self to Bidder");
                            role = ParticipantRole.PARTICIPANT;
                            addBehaviour(new BidderBehaviour());
                            done = true;
                        }
                    }
                }
            } else if (state == AuctionState.IDLE) {
                System.out.println(getLocalName() + ": Starting auction for job");
                Random r = new Random();
                int starttime = r.nextInt(Integer.MAX_VALUE);
                int endtime = r.nextInt(Integer.MAX_VALUE);
                if (starttime == endtime) {
                    endtime++;
                } else if (starttime > endtime) {
                    int space = endtime;
                    endtime = starttime;
                    starttime = space;
                }
                String id = java.util.UUID.randomUUID().toString();
                auctionedJob = new Job(id, starttime, endtime);
                ACLMessage jobOffer = new JobMessage(auctionedJob, otherParticipants);
                send(jobOffer);
                System.out.println(getLocalName() + ": Job request for "
                        + auctionedJob.toString() + " sent to bidders");
                state = AuctionState.WAITING;
            }
        }

        @Override
        public boolean done() {
            return done;
        }
    }

    private class ParticipantMessageHandler extends WaitForMessageBehaviour {

        @Override
        public void handleMessage(ACLMessage m) {
            if (m.getPerformative() == ACLMessage.INFORM && m.getContent().equals("init_auction")) {
                System.out.println(getLocalName() + ": Received message to initialize auction");
                if (role.equals(ParticipantRole.AUCTIONEER)) {
                    System.out.println("\tI will act as auctioneer");
                    addBehaviour(new AuctioneerBehaviour());
                } else {
                    addBehaviour(new BidderBehaviour());
                }
                setDone(true);
            } else if (m.getPerformative() == ACLMessage.INFORM && m.getContent().equals("promote")) {
                System.out.println(getLocalName() + ": Promoted to Auctioneer");
                role = ParticipantRole.AUCTIONEER;
            } else if (m.getPerformative() == ACLMessage.INFORM && m.getContent().equals("demote")) {
                role = ParticipantRole.PARTICIPANT;
                System.out.println(getLocalName() + ": Demoted to Participant");
            } else if (m.getPerformative() == ACLMessage.INFORM && m.getContent().startsWith("participants:")) {
                System.out.println(getLocalName() + ": Received participant list " + m.getContent());
                String participantList = m.getContent().substring("participants:[".length());
                participantList = participantList.substring(0, participantList.length() - 1);
                String[] participants = participantList.split(", ");
                for (String p : participants) {
                    if (!p.equals(getLocalName())) {
                        System.out.println(getLocalName() + ": Adding '" + p
                                + "' to list of other participants");
                        otherParticipants.add(p);
                    }
                }
            }
        }
    }

    private enum ParticipantRole {

        PARTICIPANT, AUCTIONEER;
    }

    private enum AuctionState {

        IDLE, WAITING;
    }

    private ParticipantRole role = ParticipantRole.PARTICIPANT;
    private Set<String> otherParticipants = new HashSet<>();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": Participant started");
        System.out.println("\tRequesting registration at administrator");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(Administrator.ADMINISTRATOR_LOCAL_NAME, AID.ISLOCALNAME));
        msg.setContent("register");
        send(msg);
        addBehaviour(new ParticipantMessageHandler());
    }

}
