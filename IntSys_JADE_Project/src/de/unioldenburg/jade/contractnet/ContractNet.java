package de.unioldenburg.jade.contractnet;

import de.unioldenburg.jade.contractnet.agents.Administrator;
import de.unioldenburg.jade.contractnet.agents.Participant;
import jade.wrapper.AgentContainer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 * The MAS for a ContractNet as of Excercise #2
 * @author Christoph Küpker
 */
public class ContractNet {
    
    /**
     * Main entry point to the MAS ContractNet which instantiates Agents and 
     * registers participants on the administrator (in correct order). As given
     * in the StudIP forum. Number of participants can be changed in the for 
     * loop.
     * @param args Not required
     */
    public static void main(String[] args) {
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
        profile.setParameter("gui", "true");

        // Create container
        container = runtime.createMainContainer(profile);

        // Create agents
        try {
            AgentController agent = container.createNewAgent("admin" , Administrator.class.getName(), args);
            agent.start();
            
            // MAS will work even if names or count of participants are changed
            for(int i=0; i<3; i++) {
                agent = container.createNewAgent("participant"+i, Participant.class.getName(), args);
                agent.start();
            }
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }
}
