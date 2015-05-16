package de.unioldenburg.jade.maumau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.maumau.SelectedCard;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Christoph Küpker
 */
public class Player extends Agent {

	private static final long serialVersionUID = 1L;

	public static final String DISTRIBUTE_CARD_MESSAGE_CONTENT = "distCard",
                NEXT_MESSAGE_CONTENT = "next",
                GAMEOVER_MESSAGE_CONTENT = "gameover",
                NEXT_EXECUTE_CARD_MESSAGE_CONTENT = "nextExec",
                NEXT_WISHED_CARD_MESSAGE_CONTENT = "wished",
                PLAYED_CARD_MESSAGE_CONTENT_PREFIX = "played"
                ;

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
                        String card = msg.getContent().substring(8);
                        handCards.add(card);
                        System.out.println(getLocalName() + ": Got card from "
                                + "stack. Now holding " + handCards.size()
                                + " cards in hand.");
                    } else if (msg.getContent().startsWith(
                            NEXT_EXECUTE_CARD_MESSAGE_CONTENT)) {
                        executeTurn(msg.getContent().substring(8), true);
                    } else if (msg.getContent().startsWith(NEXT_MESSAGE_CONTENT)) {
                        executeTurn(msg.getContent().substring(4), false);
                    } else if (msg.getContent().startsWith(
                            NEXT_WISHED_CARD_MESSAGE_CONTENT)) {
                        executeTurn(msg.getContent().substring(6) + "0", false);
                        // executeWishedColorTurn(msg.getContent().substring(6));
                    } else if (msg.getContent().equals(GAMEOVER_MESSAGE_CONTENT)) {
                        System.out.println(getLocalName()
                                + ": I lost :( Shutting down.. Goodbye");
                        doDelete();
                    }
                }

	}

	/**
	 * Puts a card on the openCards stack.
	 * 
	 * @param openCard
	 *            - the current upper card
	 * @param exec
	 *            - true if the player needs to react to the card (example 7 and
	 *            8), false if another player already executed the card
	 * @author Armin Pistoor
	 */
	private void executeTurn(String openCard, boolean exec) {
		SelectedCard playCard = this.getDefaultStrategy(openCard, exec);
		// check playCard
		if (!(playCard.getCard() == null)) {
			// plays card
			System.out.println(playCard.getMessage());
			ACLMessage finishedMsg = new ACLMessage(ACLMessage.PROPOSE);
			if (playCard.isJack()) {
				finishedMsg.setContent(Dealer.WISH_MESSAGE_CONTENT + playCard.getCard() + playCard.getWishedColor());
			} else {
				finishedMsg.setContent(playCard.getCard());				
			}
			finishedMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME,
					AID.ISLOCALNAME));
			send(finishedMsg);
		} else {
			// NO valid card found
			System.out.println(this.getLocalName()
					+ ": No valid card. Passing.");
			ACLMessage drawMsg = new ACLMessage(ACLMessage.REQUEST);
			drawMsg.setContent(Dealer.DRAW_MESSAGE_CONTENT);
			drawMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME,
					AID.ISLOCALNAME));
			send(drawMsg);
		}
		if (this.handCards.isEmpty()) {
			// wins game
			System.out.println(this.getLocalName() + ": Mau-Mau!");
			ACLMessage winningMsg = new ACLMessage(ACLMessage.INFORM);
			winningMsg.setContent(Dealer.WIN_MESSAGE_CONTENT);
			winningMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME,
					AID.ISLOCALNAME));
			send(winningMsg);
			System.out.println(getLocalName() + ": Shutting down.. Goodbye");
			doDelete();
		}
	}

	/**
	 * Tries to play a seven or passes if none is in hand
	 * 
	 * @author ckuepker
	 */
	private SelectedCard reactToAttack(Character attackingCardCharacter) {
		SelectedCard playCard = new SelectedCard();
		for (String card : handCards) {
			if (card.charAt(1) == attackingCardCharacter) {
				handCards.remove(card);
				playCard.setCard(card);
				playCard.setMessage(getLocalName() + ": Playing " + card
						+ " as " + "answer to the attacking "+attackingCardCharacter);
				return playCard;
			}
		}
		playCard.setCard(null);
		playCard.setMessage(getLocalName() + ": Cannot defend from attacking "+attackingCardCharacter);
		return playCard;
	}

	/**
	 * Default strategy.
	 * 
	 * @return the card to play return null if no suitable caard
	 * @author Armin Pistoor
	 */
	private SelectedCard getDefaultStrategy(String openCard, boolean exec) {
        SelectedCard playCard = new SelectedCard();
        List<Integer> jacks = new ArrayList<Integer>(4);
        for (int i = 0; i < this.handCards.size(); i++) {
            if (handCards.get(i).charAt(1) == 'B') {
                jacks.add(i);
            } else if (exec == true) {
                char reactionIdentifier = openCard.charAt(1);
                switch (reactionIdentifier) {
                    case '7': case '8':
                        return reactToAttack(reactionIdentifier);
                    default:
                        throw new IllegalArgumentException("Cannot react to "
                                + "given card " + openCard);
                }
            } else if (this.handCards.get(i).charAt(0) == openCard.charAt(0)
                    || this.handCards.get(i).charAt(1) == openCard.charAt(1)) {
                playCard.setCard(handCards.remove(i));
                playCard.setMessage(this.getLocalName() + ": playing card "
                        + playCard.getCard() + "! " + handCards.size()
                        + " cards left");
                return playCard;
            }
        }
        if (jacks.size() > 0) {
            int index = jacks.get(0);
            playCard.setCard(this.handCards.remove(index));
            playCard.setJack(true);
            jacks.clear();
            String color = this.getWishingColor();
            playCard.setMessage(this.getLocalName() + ": playing card "
                    + playCard.getCard() + "! I would like to wish the color "
                    + color + "! " + handCards.size() + " cards left");
            playCard.setWishedColor(color);
        } else {
            playCard.setCard(null);
        }
        return playCard;
    }

	/**
	 * Gets the color to wish. 
	 * @author Armin Pistoor
	 * @return the wishing color
	 */
	private String getWishingColor() {
            String[] colors = {
                "K","P","C","H"
            };
            Random r = new Random();
            return colors[r.nextInt(4)];
	}
}
