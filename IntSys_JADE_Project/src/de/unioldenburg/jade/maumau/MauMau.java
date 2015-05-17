package de.unioldenburg.jade.maumau;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import de.unioldenburg.jade.maumau.agents.Dealer;
import de.unioldenburg.jade.maumau.agents.GPlayer;
import de.unioldenburg.jade.maumau.agents.Player;
import de.unioldenburg.jade.starter.AgentRuntimeCreator;

/**
 * Entry point for the MauMau MAS which creates a JADE container with one
 * administrator agent and 3 playing agents from which one is smarter
 * 
 * @author Christoph Küpker
 */
public class MauMau {
	
	public static final int PLAYER_COUNT = 3;

	public static void main(String[] args) {
		AgentContainer container = AgentRuntimeCreator
				.createAgentContainer(false);

		// Create dealer
		// Create agents
		try {
			AgentController agent = container.createNewAgent(
					Dealer.DEALER_LOCAL_NAME, Dealer.class.getName(), null);
			agent.start();

			agent = container.createNewAgent(Player.PLAYER_LOCAL_NAME_PREFIX
					+ "0", Player.class.getName(), null);
			agent.start();
			agent = container.createNewAgent(Player.PLAYER_LOCAL_NAME_PREFIX
					+ "1", Player.class.getName(), null);
			agent.start();
			agent = container.createNewAgent(GPlayer.PLAYER_LOCAL_NAME_PREFIX
					+ "2", GPlayer.class.getName(), null);
			agent.start();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}

	}
}
