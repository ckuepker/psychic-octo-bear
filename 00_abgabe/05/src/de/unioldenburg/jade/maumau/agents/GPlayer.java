package de.unioldenburg.jade.maumau.agents;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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


	private static final long serialVersionUID = 1L;
	
	/**
	 * the open cards.
	 */
	private Stack<String> openCards;
	
	/**
	 * is there any player who has only 1 card left
	 */
	private boolean isLastCardPlayerPresent;

	private class GPlayerMessageHandler extends WaitForMessageBehaviour {


		private static final long serialVersionUID = 1L;

		@Override
		public void handleMessage(ACLMessage msg) {
			if (msg.getContent().startsWith(STARTING_CARD_MESSAGE_CONTENT)) {
				String startingCard = msg.getContent().substring(STARTING_CARD_MESSAGE_CONTENT.length());
				System.out.println(getLocalName()+"[GPlayer]: Starting card is " + startingCard);
				openCards.add(startingCard);
			} else if (msg.getContent().startsWith(DISTRIBUTE_CARD_MESSAGE_CONTENT)) {
				String card = msg.getContent().substring(8);
				handCards.add(card);
				System.out.println(getLocalName() + "[GPlayer]: Got card from "
						+ "stack. Now holding " + handCards.size()
						+ " cards in hand.");
			} else if (msg.getContent().startsWith(NEXT_EXECUTE_CARD_MESSAGE_CONTENT)) {
				executeTurn(msg.getContent().substring(8), true);
			} else if (msg.getContent().startsWith(NEXT_MESSAGE_CONTENT)) {
				executeTurn(msg.getContent().substring(4), false);
			} else if (msg.getContent().startsWith(NEXT_WISHED_CARD_MESSAGE_CONTENT)) {
				executeTurn(msg.getContent().substring(6) + "0", false);
				// executeWishedColorTurn(msg.getContent().substring(6));
			} else if (msg.getContent().equals(GAMEOVER_MESSAGE_CONTENT)) {
				System.out.println(getLocalName() + "[GPlayer]: I lost :( Shutting down.. Goodbye");
				doDelete();
			} else if (msg.getContent().startsWith(PLAYED_CARD_MESSAGE_CONTENT_PREFIX)) {
				// Falls der player noch gebraucht wird (f�r krassere KIs)
				// String player = msg.getContent().substring(6,
				// (msg.getContent().length() - 2));
				String card = msg.getContent().substring(
						(msg.getContent().length() - 2),
						msg.getContent().length());
				openCards.add(card);
			} else if (msg.getContent().equals(DECK_SHUFFLED_MESSAGE_CONTENT)) {
				System.out
						.println(getLocalName()
								+ "[GPlayer]: Deck was shuffled, clearing internal storage");
				String upperCard = openCards.peek();
				openCards.clear();
				openCards.push(upperCard);
			} else if (msg.getContent().endsWith(Dealer.LAST_CARD_MESSAGE_CONTENT)) {
				isLastCardPlayerPresent = true;
			}
          }
	}

	@Override
	protected void setup() {
		System.out.println(getLocalName() + "[GPlayer]: Player started");
		System.out.println("\tRequesting registration at dealer");
		this.handCards = new ArrayList<String>();
		this.openCards = new Stack<String>();
		this.isLastCardPlayerPresent = false;
		// Register self on administrator
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(new AID(Dealer.DEALER_LOCAL_NAME, AID.ISLOCALNAME));
		msg.setContent(Dealer.REGISTER_MESSAGE_CONTENT);
		send(msg);
		this.addBehaviour(new GPlayerMessageHandler());
		System.out.println(getLocalName() + "[GPlayer]: GONNA WIN THIS, CUZ SMART");
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
				//wins game
				System.out.println(this.getLocalName() + "[GPlayer]: Mau-Mau!");
				finishedMsg.setContent(Dealer.WIN_MESSAGE_CONTENT + playCard.getCard());
				send(finishedMsg);
				System.out.println(getLocalName() + "[GPlayer]: Shutting down.. Goodbye");
				doDelete();
			} else {
				send(finishedMsg);
			}
		} else {
			// NO valid card found
			System.out.println(this.getLocalName()
					+ "[GPlayer]: No valid card. Passing.");
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
				playCard.setMessage(getLocalName() + "[GPlayer]: Playing " + card
						+ " as " + "answer to the attacking "+attackingCardCharacter);
				return playCard;
			}
		}
		playCard.setCard(null);
		playCard.setMessage(getLocalName() + "[GPlayer]: Cannot defend from attacking "+attackingCardCharacter);
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
		Map<String, Integer> mostCommonColors = this.getColorMap();
		
		//react to 7 and 8
		if (exec == true) {
			char reactionIdentifier = openCard.charAt(1);
			switch (reactionIdentifier) {
			case '7':
			case '8':
				return reactToAttack(reactionIdentifier);
			default:
				throw new IllegalArgumentException("[GPlayer] Cannot react to "
						+ "given card " + openCard);
			}
		}
		
		if (this.isLastCardPlayerPresent) {
			this.isLastCardPlayerPresent = false;
			//Play 7
			playCard = tryPlayingSeven(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Maybe play a 8...
			playCard = tryPlayingEight(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Play the color that's the least common in game
			playCard = tryPlayingLeastCommonColor(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Maybe play a jack
			playCard = tryPlayingJack(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}			
		} else {
			//Play the color that's the least common in game and no 7 or 8
			playCard = tryPlayingLeastCommonColor(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Maybe play a 8...
			playCard = tryPlayingEight(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Maybe play a 7...
			playCard = tryPlayingSeven(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
			//Maybe play a jack
			playCard = tryPlayingJack(openCard, mostCommonColors);
			if (playCard.getCard() != null) {
				return playCard;
			}
		}

		//really no cards to play
		return playCard;
	}

	private SelectedCard tryPlayingLeastCommonColor(String openCard, Map<String, Integer> mostCommonColors) {
		SelectedCard playCard = new SelectedCard();
		for (Map.Entry<String, Integer> entry : mostCommonColors.entrySet()) {
			for (int j = 0; j < handCards.size(); j++) {
				//Dont check jacks and don't play 7 or 8
				if ((this.handCards.get(j).charAt(1) != 'B') 
						&& (this.handCards.get(j).charAt(1) != '7') 
						&& (this.handCards.get(j).charAt(1) != '8')) {
					//check card if the handcard color is the same as the least common color
					if (this.handCards.get(j).charAt(0) == entry.getKey().charAt(0)) {
						//check card if the handcard picture is the same as the openCard picture
						//or the handcard color is the same as the openCard color
						if( (this.handCards.get(j).charAt(1) == openCard.charAt(1)) 
								|| (this.handCards.get(j).charAt(0) == openCard.charAt(0)) ) {
							System.out.println(this.handCards.get(j) + " passt zu der Farbe");
							this.openCards.add(this.handCards.get(j));
							playCard.setCard(this.handCards.remove(j));
							playCard.setMessage(this.getLocalName() + "[GPlayer]: playing card "
									+ playCard.getCard() + "! " + handCards.size()
									+ " cards left");
							return playCard;										
						}
						
					}
				}
			}			
		}
		playCard.setCard(null);
		return playCard;
	}
	
	private SelectedCard tryPlayingEight(String openCard, Map<String, Integer> mostCommonColors) {
		SelectedCard playCard = new SelectedCard();
		for (Map.Entry<String, Integer> entry : mostCommonColors.entrySet()) {
			for (int j = 0; j < handCards.size(); j++) {
				//Only check 8
				if (this.handCards.get(j).charAt(1) == '8') {
					//check card if the handcard color is the same as the least common color
					if (this.handCards.get(j).charAt(0) == entry.getKey().charAt(0)) {
						//check card if the handcard picture is the same as the openCard picture
						//or the handcard color is the same as the openCard color
						if( (this.handCards.get(j).charAt(1) == openCard.charAt(1)) 
								|| (this.handCards.get(j).charAt(0) == openCard.charAt(0)) ) {
							System.out.println(this.handCards.get(j) + " passt zu der Farbe");
							this.openCards.add(this.handCards.get(j));
							playCard.setCard(this.handCards.remove(j));
							playCard.setMessage(this.getLocalName() + "[GPlayer]: playing card "
									+ playCard.getCard() + "! " + handCards.size()
									+ " cards left");
							return playCard;										
						}
						
					}
				}
			}			
		}
		playCard.setCard(null);
		return playCard;		
	}
	
	private SelectedCard tryPlayingSeven(String openCard, Map<String, Integer> mostCommonColors) {
		SelectedCard playCard = new SelectedCard();
		for (Map.Entry<String, Integer> entry : mostCommonColors.entrySet()) {
			for (int j = 0; j < handCards.size(); j++) {
				//Only check 7
				if ((this.handCards.get(j).charAt(1) == '7')) {
					//check card if the handcard color is the same as the least common color
					if (this.handCards.get(j).charAt(0) == entry.getKey().charAt(0)) {
						//check card if the handcard picture is the same as the openCard picture
						//or the handcard color is the same as the openCard color
						if( (this.handCards.get(j).charAt(1) == openCard.charAt(1)) 
								|| (this.handCards.get(j).charAt(0) == openCard.charAt(0)) ) {
							System.out.println(this.handCards.get(j) + " passt zu der Farbe");
							this.openCards.add(this.handCards.get(j));
							playCard.setCard(this.handCards.remove(j));
							playCard.setMessage(this.getLocalName() + "[GPlayer]: playing card "
									+ playCard.getCard() + "! " + handCards.size()
									+ " cards left");
							return playCard;										
						}
						
					}
				}
			}			
		}
		playCard.setCard(null);
		return playCard;
	}
	
	private SelectedCard tryPlayingJack(String openCard, Map<String, Integer> mostCommonColors) {
		SelectedCard playCard = new SelectedCard();
		for (int i = 0; i < this.handCards.size(); i++) {
			if (handCards.get(i).charAt(1) == 'B') {
				this.openCards.add(this.handCards.get(i));
				playCard.setCard(this.handCards.remove(i));
				playCard.setJack(true);
				String color = this.getWishingColor();
				playCard.setMessage(this.getLocalName() + "[GPlayer]: playing card "
						+ playCard.getCard() + "! I would like to wish the color "
						+ color + "! " + handCards.size() + " cards left");
				playCard.setWishedColor(color);
				return playCard;
			}
		}
		playCard.setCard(null);
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
		for (int i = (handCards.size() + this.openCards.size()); i >= 0; i--) {
			if (clubs == i) {
				mostCommonColors.put("K", i);
			} 
			if (spades == i) {
				mostCommonColors.put("P", i);
			} 
			if (hearts == i) {
				mostCommonColors.put("H", i);
			} 
			if (diamonds == i) {
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
