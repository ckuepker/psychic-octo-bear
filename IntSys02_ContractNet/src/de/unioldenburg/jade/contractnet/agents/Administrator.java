/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unioldenburg.jade.contractnet.agents;

import de.unioldenburg.jade.contractnet.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.contractnet.messages.ParticipantListMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ckuepker
 */
public class Administrator extends Agent {
    
    /**
     * 
     */
    private class AdministratorMessageHandler extends WaitForMessageBehaviour {
                
        @Override
        public void handleMessage(ACLMessage msg) {
            if (msg.getContent().equals("ContractNet GO")) {
                System.out.println("Administrator: Startup message found.\n\tInitiating broadcast to participants...");
                send(new ParticipantListMessage(participants));
                
                ACLMessage startupBroadcast = new ACLMessage(ACLMessage.INFORM);
                addAllParticipants(startupBroadcast);
                startupBroadcast.setContent("init_auction");
                send(startupBroadcast);
                System.out.println("\tBroadcast send");
                setDone(true);
            } else if (msg.getContent().equals("register")) {
                registerParticipant(msg.getSender().getLocalName());
            }
        }        
    }
    
    public final static String ADMINISTRATOR_LOCAL_NAME = "admin";
    
    private Set<String> participants;

    @Override
    protected void setup() {
        System.out.println("Administrator: AdministratorAgent started");
        this.participants = new HashSet<>(3);
        addBehaviour(new AdministratorMessageHandler());
    }
    
    public void registerParticipant(String participantName) {
        System.out.println("Administrator: Registering '"+participantName+"' on "
                +"Administrator");
        this.participants.add(participantName);
        if (this.participants.size() == 1) {
            System.out.println("\tPromoting '"+participantName
                    +"' to auctioneer");
            ACLMessage makeAuctioneerMessage = new ACLMessage(ACLMessage.INFORM);
            makeAuctioneerMessage.addReceiver(new AID(participantName, AID.ISLOCALNAME));
            makeAuctioneerMessage.setContent("promote");
            send(makeAuctioneerMessage);
        }
    }
    
    private ACLMessage addAllParticipants(ACLMessage message) {
        for (String participantLocalName : participants) {
            message.addReceiver(new AID(participantLocalName,AID.ISLOCALNAME));
        }
        return message;
    }
}
