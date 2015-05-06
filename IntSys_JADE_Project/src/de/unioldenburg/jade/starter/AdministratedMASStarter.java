package de.unioldenburg.jade.starter;

import de.unioldenburg.jade.contractnet.agents.Administrator;
import de.unioldenburg.jade.contractnet.agents.Participant;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
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
        String host;
        int port;
        String platform = null; // default name
        boolean main = true;

        host = "localhost";
        port = -1; // default-port 1099

        
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = null;
        AgentContainer container = null;

        profile = new ProfileImpl(host, port, platform, main);
        profile.setParameter("gui", ""+gui);

        // Create container
        container = runtime.createMainContainer(profile);

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
