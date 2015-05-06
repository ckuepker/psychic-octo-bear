package de.unioldenburg.jade.contractnet;

import de.unioldenburg.jade.contractnet.agents.Administrator;
import de.unioldenburg.jade.contractnet.agents.Participant;
import de.unioldenburg.jade.starter.AdministratedMASStarter;

/**
 * The MAS for a ContractNet as of Excercise #2
 * @author Christoph Küpker
 */
public class ContractNet {
    
    public static void main(String[] args) {
        AdministratedMASStarter.startMAS(Administrator.class, "admin",
                Participant.class, 3, "participant", true);
    }
}
