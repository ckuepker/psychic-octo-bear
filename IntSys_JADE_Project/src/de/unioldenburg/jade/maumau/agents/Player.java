package de.unioldenburg.jade.maumau.agents;

import jade.core.Agent;

/**
 *
 * @author Christoph Küpker
 */
public class Player extends Agent {

    @Override
    protected void setup() {
        System.out.println(getLocalName()+": Player started");
    }
}
