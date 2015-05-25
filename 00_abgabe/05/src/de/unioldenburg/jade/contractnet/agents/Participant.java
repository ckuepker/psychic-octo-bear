package de.unioldenburg.jade.contractnet.agents;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
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
 * Participant of a ContractNet which can act as auctioneer or bidder. Will 
 * register itself on the administrator at startup. Has to be informed about
 * all other participants of a ContractNet.
 * @author Christoph Küpker
 */
public class Participant extends Agent {

    // Keys and values for ACLMessages
    private final static String BIDDING_CONTENT = "bidding";
    private final static String ACCEPT_CONTENT = "accepted";
    private final static String KEY_BIDDING = "bidding";
    public static final String KEY_ID = "job_id";
    public static final String KEY_START = "starttime";
    public static final String KEY_END = "endtime";

    /**
     * Behaviour for a Participant which currently isn't auctioneer. Will wait 
     * for job messages and bid on them accordingly. Will promote itself to 
     * auctioneer if a bidding was accepted.
     * @author Christoph Küpker
     */
    private class BidderBehaviour extends WaitForMessageBehaviour {

        @Override
        public void handleMessage(ACLMessage m) {
            // Handle incoming Job offers
            if (m.getPerformative() == ACLMessage.REQUEST && m.getContent().equals(JobMessage.CONTENT)) {
                System.out.println(getLocalName() + ": Received job offer from auctioneer");
                // Parse job info from message
                Job job = Job.parseJobFromMessage(m);
                // Make a bidding in [starttime,endtime)
                Random r = new Random();
                int bidding = r.nextInt(job.getEndtime() - job.getStarttime()) + job.getStarttime();
                // Create reply with bidding and send it to auctioneer
                ACLMessage reply = m.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(BIDDING_CONTENT);
                reply.addUserDefinedParameter(KEY_ID, job.getId());
                reply.addUserDefinedParameter(KEY_BIDDING, "" + bidding);
                System.out.println(getLocalName() + ": Sending offer " + bidding
                        + " for " + job.toString());
                send(reply);
            // Promote self to auctioneer after bidding was accepted
            } else if (m.getPerformative() == ACLMessage.ACCEPT_PROPOSAL && m.getContent().equals(ACCEPT_CONTENT)) {
                System.out.println(getLocalName()+": My proposal was accepted. I will now act as auctioneer");
                role = ParticipantRole.AUCTIONEER;
                addBehaviour(new AuctioneerBehaviour());
                // Remove this behaviour
                setDone(true);
            }
        }
    }

    /**
     * Behaviour for Participants which act as auctioneer. Will create new jobs
     * and offer them to all Bidders and then wait for incoming biddings
     * until all Bidders have sent one. Auctioneer will then accept the 
     * highest (earliest) bidding an demote himself to Bidder.
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
            // Check for message
            ACLMessage message = receive();
            if (message != null) {
                if (message.getPerformative() == ACLMessage.PROPOSE && message.getContent().equals(BIDDING_CONTENT)) {
                    // Proposal received
                    String id = message.getUserDefinedParameter(KEY_ID);
                    // Check if correct job was referenced
                    if (auctionedJob.getId().equals(id)) {
                        System.out.println(getLocalName()+": Bidding received");
                        // Count biddings so auctioneer knows when auction is over
                        biddings++;
                        int bidding = Integer.parseInt(message.getUserDefinedParameter(KEY_BIDDING));
                        // Check if new bidding is (first or) best bidding and 
                        // store accordingly
                        if (highestBidding < 0 || bidding < highestBidding) {
                            highestBidding = bidding;
                            highestBiddingMessage = message;
                            System.out.println(getLocalName()+": New highest bidding "
                                    +highestBidding+" received from "
                                    +highestBiddingMessage.getSender().getLocalName());
                        }
                        // Check if auction is over (aka all bidders sent their offers)
                        if (biddings == otherParticipants.size()) {
                            System.out.println(getLocalName()+": All biddings received. Closing auction");
                            // Close auction
                            ACLMessage proposalAccept = highestBiddingMessage.createReply();
                            proposalAccept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                            proposalAccept.setContent(ACCEPT_CONTENT);
                            send(proposalAccept);
                            System.out.println(getLocalName()+": Proposal acceptance sent to new auctioneer. Demoting self to Bidder");
                            // Demote self to Bidder
                            role = ParticipantRole.BIDDER;
                            addBehaviour(new BidderBehaviour());
                            done = true;
                        }
                    }
                }
            // If no message arrived, check if the auction has to be started
            } else if (state == AuctionState.IDLE) {
                System.out.println(getLocalName() + ": Starting auction for job");
                // Create a new random Job
                Random r = new Random();
                int starttime = r.nextInt(Integer.MAX_VALUE);
                int endtime = r.nextInt(Integer.MAX_VALUE);
                // Consistency checks
                if (starttime == endtime) {
                    endtime++;
                } else if (starttime > endtime) {
                    int space = endtime;
                    endtime = starttime;
                    starttime = space;
                }
                String id = java.util.UUID.randomUUID().toString();
                auctionedJob = new Job(id, starttime, endtime);
                // Send job offer to bidders
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

    /**
     * Message handler for Participants before auction. Will handle the start 
     * signal and transform the agent to auctioneer or bidder if received. Also 
     * receives and handles messages containing all other participants or a 
     * promotion to auctioneer role.
     */
    private class ParticipantMessageHandler extends WaitForMessageBehaviour {

        @Override
        public void handleMessage(ACLMessage m) {
            // Auction start
            if (m.getPerformative() == ACLMessage.INFORM && m.getContent().equals("init_auction")) {
                System.out.println(getLocalName() + ": Received message to initialize auction");
                // Set behaviours according to internal role
                if (role.equals(ParticipantRole.AUCTIONEER)) {
                    System.out.println("\tI will act as auctioneer");
                    addBehaviour(new AuctioneerBehaviour());
                } else {
                    addBehaviour(new BidderBehaviour());
                }
                setDone(true);
            // Handle incoming promotion to auctioneer from administrator
            } else if (m.getPerformative() == ACLMessage.INFORM && m.getContent().equals("promote")) {
                System.out.println(getLocalName() + ": Promoted to Auctioneer");
                role = ParticipantRole.AUCTIONEER;
            // Handle incoming participant list
            } else if (m.getPerformative() == ACLMessage.INFORM && m.getContent().startsWith("participants:")) {
                System.out.println(getLocalName() + ": Received participant list " + m.getContent());
                // Ugly string parsing in here
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

    /**
     * Possible role of a Participant
     */
    private enum ParticipantRole {

        BIDDER, AUCTIONEER;
    }

    /**
     * Possible auction states
     */
    private enum AuctionState {

        IDLE, WAITING;
    }

    private ParticipantRole role = ParticipantRole.BIDDER;
    private Set<String> otherParticipants = new HashSet<>();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": Participant started");
        System.out.println("\tRequesting registration at administrator");
        // Register self on administrator
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(Administrator.ADMINISTRATOR_LOCAL_NAME, AID.ISLOCALNAME));
        msg.setContent("register");
        send(msg);
        // Add waiting behaviour before auction start
        addBehaviour(new ParticipantMessageHandler());
    }

}
