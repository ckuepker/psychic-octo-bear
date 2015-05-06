package de.unioldenburg.jade.maumau.agents;

import jade.core.Agent;

/**
 *
 * @author Christoph Küpker
 */
public class Dealer extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName()+": Dealer started.. Waiting for players");
    }
}
