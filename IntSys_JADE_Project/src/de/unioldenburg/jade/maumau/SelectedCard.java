package de.unioldenburg.jade.maumau;

/**
 * Object to save a message to the selected card to play.
 * @author Armin Pistoor
 *
 */
public class SelectedCard {
	
	/**
	 * the card.
	 */
	private String card;
	
	/**
	 * the message.
	 */
	private String message;
	
	/**
	 * is jack.
	 */
	private boolean isJack;
	
	/**
	 * the wished color.
	 */
	private String wishedColor;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isJack() {
		return isJack;
	}

	public void setJack(boolean isJack) {
		this.isJack = isJack;
	}

	public String getWishedColor() {
		return wishedColor;
	}

	public void setWishedColor(String wishedColor) {
		this.wishedColor = wishedColor;
	}
	
}
