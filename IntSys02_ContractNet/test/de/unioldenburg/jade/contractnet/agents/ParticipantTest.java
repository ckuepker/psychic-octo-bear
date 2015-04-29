package de.unioldenburg.jade.contractnet.agents;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Christoph Küpker
 */
public class ParticipantTest {
    
    private Participant agent;
    
    @Before
    public void setup() {
        agent = new Participant();
    }
    
    @Test
    public void testInitialState() {
        assertFalse(agent.isAuctioneer());
    }
}
