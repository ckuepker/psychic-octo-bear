package de.unioldenburg.jade.evaluation;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.maumau.agents.Dealer;
import de.unioldenburg.jade.maumau.agents.GPlayer;
import de.unioldenburg.jade.maumau.agents.Player;
import de.unioldenburg.jade.starter.AgentRuntimeCreator;

public class MauMauAgentEvaluator extends Agent {
	
	public final static String EVALUATOR_LOCAL_NAME = "evaluator";
	private final static int RUNS_PER_STARTING_POSITION = 10;
	private final static int[] wins = {0,0,0};
	
	private AgentContainer container;
	private int startingPosition = 0;
	private String gPlayerLocalName = Player.PLAYER_LOCAL_NAME_PREFIX + startingPosition;
	private int runs = 0;
	private boolean idle = false;
	
	@Override
	protected void setup() {
		container = getContainerController();
		addBehaviour(new WaitForMessageBehaviour() {
			
			@Override
			public void handleMessage(ACLMessage m) {
				runs++;
				String winner = m.getContent();
				if (winner.equals(gPlayerLocalName)) {
					wins[startingPosition] = wins[startingPosition] + 1;
				}
				idle = true;
			}
		});
		addBehaviour(new TickerBehaviour(this, 250) {
			
			@Override
			protected void onTick() {
				if (idle) {
					idle = false;
					startNextRun();
				}				
			}
		});
		startNextRun();
	}
	
	private void startNextRun() {
		if (runs < RUNS_PER_STARTING_POSITION) {
			createAndStartAgents();
		} else if (startingPosition < 3) {
			runs = 0;
			startingPosition++;
			gPlayerLocalName = Player.PLAYER_LOCAL_NAME_PREFIX+startingPosition;
			System.out.println("\n=========================================\n"
					+getLocalName()+": Advancing starting position to "+startingPosition
					+"\n=====================================\n\n");
			System.out.println(getLocalName()+": Advancing to starting pos. #"+startingPosition);
			createAndStartAgents();
		} else {
			System.out.println("=========================================\n"
					+ "Evaluation over\n"
					+ "Wins from pos. #0: "+wins[0] + "\n"
					+ "Wins from pos. #1: "+wins[1] + "\n"
					+ "Wins from pos. #2: "+wins[2] + "\n"
					+ "Goodbye...");
		}
	}
	
	private void createAndStartAgents() {
		System.out.println("\n=========================================\n"
				+getLocalName()+": Starting run #"+runs+"\n=====================================\n\n");
		try {
			AgentController agent = container.createNewAgent(
					Dealer.DEALER_LOCAL_NAME, Dealer.class.getName(), null);
			agent.start();
			if (startingPosition == 0) {
				agent = container.createNewAgent(GPlayer.PLAYER_LOCAL_NAME_PREFIX
						+ "0", GPlayer.class.getName(), null);
				agent.start();
			} else {
				agent = container.createNewAgent(Player.PLAYER_LOCAL_NAME_PREFIX
						+ "0", Player.class.getName(), null);
				agent.start();
			}
			if (startingPosition == 1) {
				agent = container.createNewAgent(GPlayer.PLAYER_LOCAL_NAME_PREFIX
						+ "1", GPlayer.class.getName(), null);
				agent.start();
			} else {
				agent = container.createNewAgent(Player.PLAYER_LOCAL_NAME_PREFIX
						+ "1", Player.class.getName(), null);
				agent.start();
			}
			if (startingPosition == 2) {
				agent = container.createNewAgent(GPlayer.PLAYER_LOCAL_NAME_PREFIX
						+ "2", GPlayer.class.getName(), null);
				agent.start();
			} else {
				agent = container.createNewAgent(Player.PLAYER_LOCAL_NAME_PREFIX
						+ "2", Player.class.getName(), null);
				agent.start();
			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AgentContainer container = AgentRuntimeCreator
			.createAgentContainer(false);
		
		try {
			AgentController agent = container.createNewAgent(
					EVALUATOR_LOCAL_NAME, MauMauAgentEvaluator.class.getName(), null);
			agent.start();
		} catch (StaleProxyException e) {
			throw new RuntimeException(e);
		}
	}
}
