/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.unioldenburg.jade.contractnet.agents;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.contractnet.messages.ParticipantListMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.HashSet;
import java.util.Set;

/**
 * Agent which will act as Administrator to initiate the auctions of the ContractNet
 * if it receives the according message and register all Participants.
 * @author ckuepker
 */
public class Administrator extends Agent {
    
    /**
     * Handler for any incoming messages, whether to start the ContractNet auctions
     * or to register new participants
     */
    private class AdministratorMessageHandler extends WaitForMessageBehaviour {
                
        @Override
        public void handleMessage(ACLMessage msg) {
            // Start the ContractNet
            if (msg.getContent().equals("ContractNet GO")) {
                System.out.println("Administrator: Startup message found.\n\tInitiating broadcast to participants...");
                // Inform all participants about all participants so that each 
                // of them knows the name of every other participant
                send(new ParticipantListMessage(participants));
                // Signal auction start to all participants
                ACLMessage startupBroadcast = new ACLMessage(ACLMessage.INFORM);
                addAllParticipants(startupBroadcast);
                startupBroadcast.setContent("init_auction");
                send(startupBroadcast);
                System.out.println("\tBroadcast send");
                setDone(true);
            // Register a new participant to the list of participants
            } else if (msg.getContent().equals("register")) {
                registerParticipant(msg.getSender().getLocalName());
            }
        }        
    }
    
    public final static String ADMINISTRATOR_LOCAL_NAME = "admin";
    
    private Set<String> participants;

    @Override
    protected void setup() {
        System.out.println("Administrator: Administrator started");
        this.participants = new HashSet<>(3);
        addBehaviour(new AdministratorMessageHandler());
    }
    
    /**
     * Adds new participants to the internal list of participants. The first 
     * participant to be registered will also be promoted to auctioneer role.
     * @param participantName local name of the participating agent
     */
    private void registerParticipant(String participantName) {
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
    
    /**
     * Adds all participants from the internal list as receivers to the given 
     * message
     * @param message Message
     * @return message with added receivers
     */
    private ACLMessage addAllParticipants(ACLMessage message) {
        for (String participantLocalName : participants) {
            message.addReceiver(new AID(participantLocalName,AID.ISLOCALNAME));
        }
        return message;
    }
}
