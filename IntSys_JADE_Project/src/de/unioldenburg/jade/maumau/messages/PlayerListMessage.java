package de.unioldenburg.jade.maumau.messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

/**
 * Message used to inform all participants about all participants.
 * @author Christoph Küpker // customized by Armin Pistoor
 */
public class PlayerListMessage extends ACLMessage {
    
    public PlayerListMessage(ArrayList<String> playerList) {
        setPerformative(INFORM);
        System.out.println("Preparing player list broadcast");
        setContent("player:" + playerList.toString());
        for (String player : playerList) {
            this.addReceiver(new AID(player, AID.ISLOCALNAME));
        }
    }
}
