package de.unioldenburg.jade.maumau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;

/**
 *
 * @author Christoph Küpker
 */
public class Player extends Agent {

    private static final long serialVersionUID = 1L;
    
    public static final String DISTRIBUTE_CARD_MESSAGE_CONTENT = "distCard",
            NEXT_MESSAGE_CONTENT = "next",
            GAMEOVER_MESSAGE_CONTENT = "gameover";
    
    public static final String PLAYER_LOCAL_NAME_PREFIX = "player";
    

    /**
     * the hand cards.
     */
    private ArrayList<String> handCards;

    @Override
    protected void setup() {
        System.out.println(getLocalName() + ": Player started");
        System.out.println("\tRequesting registration at dealer");
        this.handCards = new ArrayList<String>();
        // Register self on administrator
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
        msg.setContent(Dealer.REGISTER_MESSAGE_CONTENT);
        send(msg);
        this.addBehaviour(new PlayerMessageHandler());
    }

    private class PlayerMessageHandler extends WaitForMessageBehaviour {

        private static final long serialVersionUID = 1L;

        @Override
        public void handleMessage(ACLMessage msg) {
            if (msg.getContent().startsWith(DISTRIBUTE_CARD_MESSAGE_CONTENT)) {
                handCards.add(msg.getContent().substring(8));
            } else if (msg.getContent().startsWith(NEXT_MESSAGE_CONTENT)) {
                executeTurn(msg.getContent().substring(4));
            } else if (msg.getContent().equals(GAMEOVER_MESSAGE_CONTENT)) {
                System.out.println(getLocalName()+": I lost :( Shutting down.. Goodbye");
                doDelete();
            }
        }

    }

    /**
     * Puts a card on the openCards stack.
     *
     * @param openCard - the current upper card
     * @author Armin Pistoor
     */
    private void executeTurn(String openCard) {
        boolean validCard = false;
        for (int i = 0; i < this.handCards.size(); i++) {
            if (this.handCards.get(i).charAt(0) == openCard.charAt(0) 
                    || this.handCards.get(i).charAt(1) == openCard.charAt(1)) {
                String playedCard = handCards.get(i);
                this.handCards.remove(i);
                System.out.println(this.getLocalName() + ": playing card " 
                        + playedCard + "! " + handCards.size() + " cards left");
                if (this.handCards.isEmpty()) {
                    System.out.println(this.getLocalName() + ": Mau-Mau!");
                    ACLMessage winningMsg = new ACLMessage(ACLMessage.INFORM);
                    winningMsg.setContent(Dealer.WIN_MESSAGE_CONTENT);
                    winningMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
                    send(winningMsg);
                    System.out.println(getLocalName()+": Shutting down.. Goodbye");
                    doDelete();
                } else {
                    this.sendFinishedMsg(playedCard);
                }
                validCard = true;
                break;
            }
        }
        if (validCard == false) {
            System.out.println(this.getLocalName() 
                    + ": No valid card. Drawing 1 card. " 
                    + (this.handCards.size() + 1) + " cards left");
            ACLMessage drawMsg = new ACLMessage(ACLMessage.REQUEST);
            drawMsg.setContent(Dealer.DRAW_MESSAGE_CONTENT);
            drawMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
            send(drawMsg);
        }
    }

    /**
     * Sends the played card to the dealer.
     *
     * @param playedCard - the played card
     * @author Armin Pistoor
     */
    private void sendFinishedMsg(String playedCard) {
        ACLMessage finishedMsg = new ACLMessage(ACLMessage.REQUEST);
        finishedMsg.setContent(playedCard);
        finishedMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
        send(finishedMsg);
    }
}
