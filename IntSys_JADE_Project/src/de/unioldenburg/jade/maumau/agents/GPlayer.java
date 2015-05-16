package de.unioldenburg.jade.maumau.agents;

import java.util.Set;

/**
 * The smarter MauMau agent
 *
 * @author Christoph Küpker, Armin Pistoor
 */
public class GPlayer {
    
    private Set<String> handCards = null;

    /**
     * Gets the color to wish.
     *
     * @author Armin Pistoor
     * @return the wishing color
     */
    //Optimierungsbedarf
    private String getWishingColor() {
        int clubs = 0; //Kreuzkarten
        int spades = 0; //Pikkarten
        int hearts = 0; //Herzkarten
        int diamonds = 0; //Karokarten
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
