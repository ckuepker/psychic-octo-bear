package de.unioldenburg.jade.maumau;

import de.unioldenburg.jade.maumau.agents.Dealer;
import de.unioldenburg.jade.maumau.agents.Player;
import de.unioldenburg.jade.starter.AdministratedMASStarter;

/**
 * Entry point for the MauMau MAS which creates a JADE container with one
 * administrator agent and 4 playing agents.
 * @author Christoph Küpker
 */
public class MauMau {
    
    public static void main(String[] args) {
        AdministratedMASStarter.startMAS(Dealer.class, "dealer", Player.class, 
                4, "player", true);
    }
}
