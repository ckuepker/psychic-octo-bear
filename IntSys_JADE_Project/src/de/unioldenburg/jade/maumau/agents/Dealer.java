package de.unioldenburg.jade.maumau.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.maumau.MauMau;

/**
 * 
 * @author Christoph Küpker
 */
public class Dealer extends Agent {

	public static final String REGISTER_MESSAGE_CONTENT = "register",
			DRAW_MESSAGE_CONTENT = "pass", WIN_MESSAGE_CONTENT = "win",
			WISH_MESSAGE_CONTENT = "wish";

	private static final long serialVersionUID = 1L;

	/**
	 * Local dealer name.
	 */
	public final static String DEALER_LOCAL_NAME = "dealer";

	/**
	 * List of player.
	 */
	private ArrayList<String> players;

	/**
	 * deck with cards.
	 */
	private Stack<String> deck;

	/**
	 * Stack with open cards.
	 */
	private Stack<String> openCards;

	/**
	 * Number of turns.
	 */
	private int numberOfTurns = 0, numberOfSevens = 0, numberOfEights = 0;

	/**
	 * The wished color (Bube).
	 */
	private String wishedColor = "0";

	@Override
	protected void setup() {
		System.out.println(getLocalName()
				+ ": Dealer started.. Waiting for players");
		this.players = new ArrayList<String>();
		this.addBehaviour(new DealerMessageHandler());
	}

	/**
	 * Handles messages.
	 * 
	 * @author Armin Pistoor
	 */
	private class DealerMessageHandler extends WaitForMessageBehaviour {

		private static final long serialVersionUID = 1L;

		@Override
		public void handleMessage(ACLMessage msg) {
			if (msg.getContent().equals(REGISTER_MESSAGE_CONTENT)) {
				registerPlayer(msg.getSender().getLocalName());
				if (players.size() == MauMau.PLAYER_COUNT) {
					startGame();
				}
			} else if (msg.getContent().equals(DRAW_MESSAGE_CONTENT)) {
				numberOfTurns++;
				if (numberOfSevens > 0) {
					String sender = msg.getSender().getLocalName();
					int amount = 2 * numberOfSevens;
					System.out.println(getLocalName() + ": " + sender
							+ " has to " + "take " + amount + " cards");
					for (int i = 0; i < amount; i++) {
						distributeOneCard(sender);
					}
					numberOfSevens = 0;
				} else if (numberOfEights > 0) {
					// NOOP
					System.out.println(getLocalName() + ": "
							+ msg.getSender().getLocalName() + " has to wait "
							+ "one round");
					numberOfEights = 0;
				} else {
					distributeOneCard(msg.getSender().getLocalName());
				}
				setNextPlayersTurn(msg);
			} else if (msg.getContent().startsWith(WISH_MESSAGE_CONTENT)) {
				wishedColor = msg.getContent().substring(6);
				String card = msg.getContent().substring(4, 6);
				System.out.println("card at dealer: " + card);
				System.out.println(getLocalName() + ": "
						+ msg.getSender().getLocalName() + " wishes the color "
						+ wishedColor);
				openCards.add(card);
				numberOfTurns++;
				setNextPlayersTurn(msg);
			} else if (msg.getContent().startsWith(WIN_MESSAGE_CONTENT)) {
				String card = msg.getContent().substring(3);
				System.out.println(getLocalName() + ": "
						+ msg.getSender().getLocalName() + " plays " + card
						+ ". He won the game!");
				ACLMessage gameoverMessage = new ACLMessage(ACLMessage.INFORM);
				for (String localName : players) {
					if (!localName.equals(msg.getSender().getLocalName())) {
						gameoverMessage.addReceiver(new AID(localName,
								AID.ISLOCALNAME));
					}
				}
				gameoverMessage.setContent(Player.GAMEOVER_MESSAGE_CONTENT);
				send(gameoverMessage);
				// shutdown
				System.out
						.println(getLocalName() + ": Shutting down.. Goodbye");
				doDelete();
			} else {
				// Card played
				numberOfTurns++;
				String playedCard = msg.getContent();
				broadcastPlayedCard(msg.getSender().getLocalName(), playedCard);
				openCards.add(playedCard);
				if (playedCard.endsWith("7")) {
					numberOfSevens++;
				} else if (playedCard.endsWith("8")) {
					numberOfEights++;
				}
				setNextPlayersTurn(msg);
			}
		}

		private void startGame() {
			initDeck();
			distributeCards();
			// Turn the upper card of the deck as beginning card
			openCards = new Stack<String>();
			openCards.add(deck.pop());
			System.out.println("\n" + getLocalName() + ": ----- Round 1 -----");
			System.out.println(getLocalName() + ": First Card is "
					+ openCards.get(openCards.size() - 1));
			// Send Message to first player to execute his turn. Message
			// contains the upper card of the open cards
			ACLMessage starterMsg = new ACLMessage(ACLMessage.INFORM);
			starterMsg.setContent(Player.NEXT_MESSAGE_CONTENT
					+ openCards.peek());
			AID firstPlayer = new AID(players.get(0), AID.ISLOCALNAME);
			System.out.println(getLocalName() + ": "
					+ firstPlayer.getLocalName() + " starts the game");
			starterMsg.addReceiver(firstPlayer);
			send(starterMsg);
		}
	}

	/**
	 * Adds new player to the list of players.
	 * 
	 * @param playerName
	 *            - local name of the player
	 */
	private void registerPlayer(String playerName) {
		System.out.println(getLocalName() + ": Registering '" + playerName
				+ "' on Dealer");
		this.players.add(playerName);
	}

	/**
	 * Initiates the card deck and shuffles it.
	 * 
	 * @author Armin Pistoor
	 */
	private void initDeck() {
		System.out.println(getLocalName() + ": Shuffling Cards");
		this.deck = new Stack<String>();
		// K = Kreuz, P=Pik, H=Herz, C=Karo
		String[] suits = { "K", "P", "H", "C" };
		for (String suit : suits) {
			for (int i = 7; i < 10; i++) {
				this.deck.add(suit + i);
			}
			// Z = Zehn, B = Bube, D = Dame, K = K�nig, A = Ass
			this.deck.add(suit + "Z");
			this.deck.add(suit + "B");
			this.deck.add(suit + "D");
			this.deck.add(suit + "K");
			this.deck.add(suit + "A");
		}
		Collections.shuffle(this.deck);
	}

	/**
	 * Distributes 6 cards to each player
	 * 
	 * @author Armin Pistoor
	 */
	private void distributeCards() {
		System.out.println(getLocalName() + ": Distributing Cards");
		for (int i = 0; i < 5; i++) {
			for (String playerLocalName : this.players) {
				this.distributeOneCard(playerLocalName);
			}
		}
	}

	/**
	 * Distributes one card to the certain player.
	 * 
	 * @author Armin Pistoor
	 */
	private void distributeOneCard(String playerLocalName) {
		// Send Message with Card to player
		try {
			reshuffleIfDeckEmpty();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ACLMessage newCardMsg = new ACLMessage(ACLMessage.INFORM);
		newCardMsg.setContent(Player.DISTRIBUTE_CARD_MESSAGE_CONTENT
				+ this.deck.pop());
		newCardMsg.addReceiver(new AID(playerLocalName, AID.ISLOCALNAME));
		send(newCardMsg);
	}

	/**
	 * Punishes a player for using a invalid card. He will get the used card
	 * back and needs to take another one.
	 * 
	 * @author Armin Pistoor
	 */
	private void punishPlayer(String playerLocalName, String card) {
		System.out.println(this.getLocalName()
				+ ": Invalid card played! The card will be returned to "
				+ playerLocalName + " and he will receive a penalty card!");
		// Return invalid card
		ACLMessage invalidCardMsg = new ACLMessage(ACLMessage.INFORM);
		invalidCardMsg
				.setContent(Player.DISTRIBUTE_CARD_MESSAGE_CONTENT + card);
		invalidCardMsg.addReceiver(new AID(playerLocalName, AID.ISLOCALNAME));
		send(invalidCardMsg);
		// Distribute penalty card
		this.distributeOneCard(playerLocalName);
	}

	/**
	 * Gets the next player
	 * 
	 * @return the next player
	 * @author Armin Pistoor
	 */
	private String getNextPlayer(String lastPlayer) {
		String nextPlayer = "";
		for (int i = 0; i < players.size(); i++) {
			if (lastPlayer.equals(players.get(i))) {
				if (i == players.size() - 1) {
					nextPlayer = players.get(0);
				} else {
					nextPlayer = players.get(i + 1);
				}
			}
		}
		return nextPlayer;
	}

	/**
	 * Checks if the deck is empty.
	 * 
	 * @author Armin Pistoor
	 * @throws Exception
	 */
	private void reshuffleIfDeckEmpty() throws Exception {
		if (this.deck.size() == 0) {
			if ((this.deck.size() + this.openCards.size()) < 2) {
				throw new Exception("Game over! No more cards to distribute!");
			}
			System.out
					.println(this.getLocalName()
							+ ": No more cards in deck! Shuffling open cards for a new deck");
			String upperCard = this.openCards.pop();
			for (String card : this.openCards) {
				this.deck.add(card);
			}
			Collections.shuffle(deck);
			openCards.clear();
			openCards.add(upperCard);
		}
	}

	/**
	 * Sends the next player the message for executing his turn.
	 * 
	 * @param msg
	 * @author Armin Pistoor
	 */
	private void setNextPlayersTurn(ACLMessage msg) {
		ACLMessage nextTurnMsg = new ACLMessage(ACLMessage.INFORM);
		String identifier;
		AID nextPlayer = new AID(getNextPlayer(msg.getSender().getLocalName()),
				AID.ISLOCALNAME);
		nextTurnMsg.addReceiver(nextPlayer);
		if ((this.numberOfTurns % players.size()) == 0) {
			System.out.println();
			System.out.println(this.getLocalName() + ": ----- Round "
					+ ((this.numberOfTurns / players.size()) + 1) + " -----");
		}
		if (openCards.get(openCards.size() - 1).charAt(1) == 'B') {
			identifier = Player.NEXT_WISHED_CARD_MESSAGE_CONTENT;
			nextTurnMsg.setContent(identifier + wishedColor);
			System.out.println(getLocalName() + ": It's "
					+ nextPlayer.getLocalName() + "s turn. "
					+ "The wished color is " + wishedColor);
		} else {
			if (numberOfEights > 0 || numberOfSevens > 0) {
				identifier = Player.NEXT_EXECUTE_CARD_MESSAGE_CONTENT;
			} else {
				identifier = Player.NEXT_MESSAGE_CONTENT;
			}
			nextTurnMsg.setContent(identifier
					+ openCards.get(openCards.size() - 1));
			System.out.println(getLocalName() + ": It's "
					+ nextPlayer.getLocalName() + "s turn. "
					+ "The upper card is "
					+ openCards.get(openCards.size() - 1));
		}
		send(nextTurnMsg);
	}

	/**
	 * Informs all players about a card played and the player that played it.
	 * The player himself is not informed (again).
	 * 
	 * @author Christoph Küpker
	 * @param playerLocalName
	 *            Local name of the player that played the card
	 * @param card
	 *            The played card
	 */
	private void broadcastPlayedCard(String playerLocalName, String card) {
		System.out.println(getLocalName() + ": Informing players that "
				+ playerLocalName + " played " + card);
		ACLMessage broadcast = new ACLMessage(ACLMessage.INFORM);
		broadcast.setContent(Player.PLAYED_CARD_MESSAGE_CONTENT_PREFIX + " "
				+ playerLocalName + " " + card);
		for (String player : players) {
			if (!player.equals(playerLocalName)) {
				broadcast.addReceiver(new AID(player, AID.ISLOCALNAME));
			}
		}
		send(broadcast);
	}
}
