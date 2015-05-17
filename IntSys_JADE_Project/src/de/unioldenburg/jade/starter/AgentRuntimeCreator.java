package de.unioldenburg.jade.starter;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;

public class AgentRuntimeCreator {
	
	public static AgentContainer createAgentContainer(boolean gui) {
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
        return container;
	}

}
