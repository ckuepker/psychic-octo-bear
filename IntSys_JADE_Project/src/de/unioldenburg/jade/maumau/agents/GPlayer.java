package de.unioldenburg.jade.maumau.agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.maumau.SelectedCard;

/**
 * The smarter MauMau agent
 * 
 * @author Christoph Küpker, Armin Pistoor
 */
public class GPlayer extends Player {

	/**
	 * the open cards.
	 */
	private Stack<String> openCards;

	private class GPlayerMessageHandler extends WaitForMessageBehaviour {

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
			} else if (msg.getContent().startsWith(
					PLAYED_CARD_MESSAGE_CONTENT_PREFIX)) {
				// Falls der player noch gebraucht wird (f�r krassere KIs)
				// String player = msg.getContent().substring(6,
				// (msg.getContent().length() - 2));
				String card = msg.getContent().substring(
						(msg.getContent().length() - 2),
						msg.getContent().length());
				openCards.add(card);
			}
		}
	}

	@Override
	protected void setup() {
		System.out.println(getLocalName() + ": Player started");
		System.out.println("\tRequesting registration at dealer");
		this.handCards = new ArrayList<String>();
		this.openCards = new Stack<String>();
		// Register self on administrator
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
		msg.setContent(Dealer.REGISTER_MESSAGE_CONTENT);
		send(msg);
		this.addBehaviour(new GPlayerMessageHandler());
		System.out.println(getLocalName() + ": GONNA WIN THIS, CUZ SMART");
	}

	@Override
	protected void executeTurn(String openCard, boolean exec) {
		SelectedCard playCard = this.getIntelligentStrategy(openCard, exec);
		// check playCard
		if (!(playCard.getCard() == null)) {
			// plays card
			System.out.println(playCard.getMessage());
			ACLMessage finishedMsg = new ACLMessage(ACLMessage.PROPOSE);
			if (playCard.isJack()) {
				finishedMsg.setContent(Dealer.WISH_MESSAGE_CONTENT
						+ playCard.getCard() + playCard.getWishedColor());
			} else {
				finishedMsg.setContent(playCard.getCard());
			}
			finishedMsg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME,
					AID.ISLOCALNAME));
			if (this.handCards.isEmpty()) {
				// wins game
				System.out.println(this.getLocalName() + ": Mau-Mau!");
				finishedMsg.setContent(Dealer.WIN_MESSAGE_CONTENT
						+ playCard.getCard());
				send(finishedMsg);
				System.out
						.println(getLocalName() + ": Shutting down.. Goodbye");
				doDelete();
			} else {
				send(finishedMsg);
			}
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
	}

	@Override
	protected SelectedCard reactToAttack(Character attackingCardCharacter) {
		SelectedCard playCard = new SelectedCard();
		for (String card : handCards) {
			if (card.charAt(1) == attackingCardCharacter) {
				this.openCards.add(card);
				handCards.remove(card);
				playCard.setCard(card);
				playCard.setMessage(getLocalName() + ": Playing " + card
						+ " as " + "answer to the attacking "
						+ attackingCardCharacter);
				return playCard;
			}
		}
		playCard.setCard(null);
		playCard.setMessage(getLocalName() + ": Cannot defend from attacking "
				+ attackingCardCharacter);
		return playCard;
	}

	/**
	 * more intelligent Strategy to play cards.
	 * 
	 * @author Armin Pistoor
	 * @param openCard
	 *            - the open card
	 * @param exec
	 *            - attacking 7 or 8 or not
	 * @return the card to select
	 */
	protected SelectedCard getIntelligentStrategy(String openCard, boolean exec) {
		SelectedCard playCard = new SelectedCard();
		List<Integer> jacks = new ArrayList<Integer>(4);
		Map<String, Integer> mostCommonColors = this.getColorMap();
		
		//react to 7 and 8
		if (exec == true) {
			char reactionIdentifier = openCard.charAt(1);
			switch (reactionIdentifier) {
			case '7':
			case '8':
				return reactToAttack(reactionIdentifier);
			default:
				throw new IllegalArgumentException("Cannot react to "
						+ "given card " + openCard);
			}
		}
			
		//Find Jacks
		for (int i = 0; i < this.handCards.size(); i++) {
			if (handCards.get(i).charAt(1) == 'B') {
				jacks.add(i);
			}
		}
		
		//Play the color that's the least common in game
		for (Map.Entry<String, Integer> entry : mostCommonColors.entrySet()) {
			for (int j = 0; j < handCards.size(); j++) {
				//if the handcard color is the same as the least common color
				//and the handcard picture is the same as the openCard picture
				//or the handcard color is the same as the openCard color --> play that card
				if ((this.handCards.get(j).charAt(0) == entry.getKey().charAt(0)) 
						&& ((this.handCards.get(j).charAt(1) == openCard.charAt(1)) 
								|| (this.handCards.get(j).charAt(0) == openCard.charAt(0)))) {
					this.openCards.add(this.handCards.get(j));
					playCard.setCard(this.handCards.get(j));
					playCard.setMessage(this.getLocalName() + ": playing card "
							+ playCard.getCard() + "! " + handCards.size()
							+ " cards left");
					return playCard;										
				}
			}			
		}
		
		
		
		
		
		
//		for (int i = 0; i < this.handCards.size(); i++) {
//			//Find Jacks
//			if (handCards.get(i).charAt(1) == 'B') {
//				jacks.add(i);
//			} else if (exec == true) {
//				char reactionIdentifier = openCard.charAt(1);
//				switch (reactionIdentifier) {
//				case '7':
//				case '8':
//					return reactToAttack(reactionIdentifier);
//				default:
//					throw new IllegalArgumentException("Cannot react to "
//							+ "given card " + openCard);
//				}
//			} else if (this.handCards.get(i).charAt(0) == openCard.charAt(0)
//					|| this.handCards.get(i).charAt(1) == openCard.charAt(1)) {
//				this.openCards.add(handCards.get(i));
//				playCard.setCard(handCards.remove(i));
//				playCard.setMessage(this.getLocalName() + ": playing card "
//						+ playCard.getCard() + "! " + handCards.size()
//						+ " cards left");
//				return playCard;
//			}
//		}
		
		//No card to play - check if there is a jack
		if (jacks.size() > 0) {
			int index = jacks.get(0);
			this.openCards.add(this.handCards.get(index));
			playCard.setCard(this.handCards.remove(index));
			playCard.setJack(true);
			jacks.clear();
			String color = this.getWishingColor();
			playCard.setMessage(this.getLocalName() + ": playing card "
					+ playCard.getCard() + "! I would like to wish the color "
					+ color + "! " + handCards.size() + " cards left");
			playCard.setWishedColor(color);
		} else {
			//really no cards to play
			playCard.setCard(null);
		}
		return playCard;
	}

	/**
	 * Generates a Map with the different colors and the related number sorted by commonness.
	 * @author Armin Pistoor
	 * @return the map
	 */
	private Map<String, Integer> getColorMap() {
		int clubs = 0; // Kreuzkarten
		int spades = 0; // Pikkarten
		int hearts = 0; // Herzkarten
		int diamonds = 0; // Karokarten
		
		//Count cards in openCards
		for (String card : this.openCards) {
			if (card.charAt(1) != 'B') {
				if (card.charAt(0) == 'K') {
					clubs++;
				} else if (card.charAt(0) == 'P') {
					spades++;
				} else if (card.charAt(0) == 'H') {
					hearts++;
				} else if (card.charAt(0) == 'C') {
					diamonds++;
				}
			}
		}
		
		//Count cards in hand
		for (String card : this.handCards) {
			if (card.charAt(1) != 'B') {
				if (card.charAt(0) == 'K') {
					clubs++;
				} else if (card.charAt(0) == 'P') {
					spades++;
				} else if (card.charAt(0) == 'H') {
					hearts++;
				} else if (card.charAt(0) == 'C') {
					diamonds++;
				}
			}
		}
		
		//Create Map
		Map<String, Integer> mostCommonColors = new LinkedHashMap<String, Integer>();
		for (int i = (handCards.size() + this.openCards.size()); i > 0; i--) {
			if (clubs == i) {
				System.out.println("jetzt kreuz " + i);
				mostCommonColors.put("K", i);
			} 
			if (spades == i) {
				System.out.println("jetzt pik " + i);
				mostCommonColors.put("P", i);
			} 
			if (hearts == i) {
				System.out.println("jetzt herz " + i);
				mostCommonColors.put("H", i);
			} 
			if (diamonds == i) {
				System.out.println("jetzt karo " + i);
				mostCommonColors.put("C", i);
			}
		}
		return mostCommonColors;
	}

	/**
	 * Gets the color to wish.
	 * 
	 * @author Armin Pistoor
	 * @return the wishing color
	 */
	// Optimierungsbedarf
	private String getWishingColor() {
		int clubs = 0; // Kreuzkarten
		int spades = 0; // Pikkarten
		int hearts = 0; // Herzkarten
		int diamonds = 0; // Karokarten
		for (String card : this.handCards) {
			if (card.charAt(1) != 'B') {
				if (card.charAt(0) == 'K') {
					clubs++;
				} else if (card.charAt(0) == 'P') {
					spades++;
				} else if (card.charAt(0) == 'H') {
					hearts++;
				} else if (card.charAt(0) == 'C') {
					diamonds++;
				}
			}
		}
		for (int i = handCards.size(); i > 0; i--) {
			if (clubs == i) {
				return "K";
			} else if (spades == i) {
				return "P";
			} else if (hearts == i) {
				return "H";
			} else if (diamonds == i) {
				return "C";
			}
		}
		return null;
	}

}
