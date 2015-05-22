package de.unioldenburg.jade.starter;

import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * Starter that can be used to start a JADE container with one administrating
 * Agent and multiple participants. Does not handle registration of Agents on 
 * each other.
 * @author Christoph Küpker
 */
public class AdministratedMASStarter {

    /**
     * Main entry point to an administrated MAS with multiple participants 
     * and one administrator as for instance in ContractNet or MauMau. 
     * As given in the StudIP forum. 
     * @param administratorClass Class of the administrator Agent
     * @param administratorName Local name of the administrator Agent
     * @param participantClass Class of the participants Agent
     * @param participantCount Number of participants to be created
     * @param participantLocalNamePrefix Prefix for the local name of all 
     * participants, will be suffixed by the number of the participant, starting 
     * at 0
     * @param gui Whether to show or not show the JADE gui
     */
    public static void startMAS(Class<? extends Agent> administratorClass,
            String administratorName, Class<? extends Agent> participantClass,
            int participantCount, String participantLocalNamePrefix,
            boolean gui) {
        AgentContainer container = AgentRuntimeCreator.createAgentContainer(gui);

        // Create agents
        try {
            AgentController agent = container.createNewAgent(administratorName, 
                    administratorClass.getName(), null);
            agent.start();
            
            // MAS will work even if names or count of participants are changed
            for(int i=0; i<participantCount; i++) {
                agent = container.createNewAgent(participantLocalNamePrefix+i, 
                        participantClass.getName(), null);
                agent.start();
            }
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
