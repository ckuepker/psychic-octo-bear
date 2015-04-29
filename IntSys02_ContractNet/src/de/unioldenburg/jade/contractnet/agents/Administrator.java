/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unioldenburg.jade.contractnet.agents;

import jade.core.Agent;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ckuepker
 */
public class Administrator extends Agent {
    
    private Set<Participant> participants;
    private Participant auctioneer;

    @Override
    protected void setup() {
        System.out.println("AdministratorAgent started");
        this.participants = new HashSet<>(3);
    }
    
    public void registerParticipant(Participant p) {
        System.out.println("Registering Participant '"+p.getLocalName()+"' on "
                +"Administrator");
        this.participants.add(p);
        if (this.participants.size() == 1) {
            System.out.println("Making newly registered Participant '"+p.getLocalName()
                    +"' the auctioneer");
            this.auctioneer = p;
            p.setAuctioneer();
        }
    }
    
    public Participant getAuctioneer() {
        return this.auctioneer;
    }
}
