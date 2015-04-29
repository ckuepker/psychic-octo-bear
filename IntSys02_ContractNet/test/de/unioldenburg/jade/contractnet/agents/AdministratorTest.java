package de.unioldenburg.jade.contractnet.agents;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Christoph Küpker
 */
public class AdministratorTest {
    
    private Administrator admin;
    
    @Before
    public void setup() {
        admin = new Administrator();
        admin.setup();
    }
    
    @Test
    public void testRegisterParticipant() {
        Participant p1 = new Participant();
        Participant p2 = new Participant();
        
        admin.registerParticipant(p1);
        admin.registerParticipant(p2);
        
        assertTrue(p1.isAuctioneer());
        assertFalse(p2.isAuctioneer());
        assertEquals(admin.getAuctioneer(), p1);
    }
    
}
