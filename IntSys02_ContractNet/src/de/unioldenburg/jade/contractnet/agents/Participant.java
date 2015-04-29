package de.unioldenburg.jade.contractnet.agents;

import jade.core.Agent;

/**
 *
 * @author Christoph Küpker
 */
public class Participant extends Agent {
    
    private enum ParticipantRole {
        PARTICIPANT, AUCTIONEER;
    }
    
    private ParticipantRole role = ParticipantRole.PARTICIPANT;
    
    @Override
    protected void setup() {
        System.out.println("Participant started");
    }
    
    public void setAuctioneer() {
        this.role = ParticipantRole.AUCTIONEER;
    }
    
    public boolean isAuctioneer() {
        return this.role.equals(ParticipantRole.AUCTIONEER);
    }
    
}
