package de.unioldenburg.jade.maumau.behaviours;

import jade.lang.acl.ACLMessage;
import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;
import de.unioldenburg.jade.maumau.agents.Player;

/**
 * @author ckuepker
 * 
 */
public abstract class MauMauPlayerBehaviour extends WaitForMessageBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2932285930989468403L;
	
	@Override
	public void handleMessage(ACLMessage msg) {
		if (msg.getContent().startsWith(Player.DISTRIBUTE_CARD_MESSAGE_CONTENT)) {
			this.receiveCard(msg.getContent().substring(8));
        } else if (msg.getContent().startsWith(Player.NEXT_EXECUTE_CARD_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(8), true);
        } else if (msg.getContent().startsWith(Player.NEXT_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(4), false);
        } else if (msg.getContent().startsWith(Player.NEXT_WISHED_CARD_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(6) + "0", false);
        } else if (msg.getContent().equals(Player.GAMEOVER_MESSAGE_CONTENT)) {
        	this.shutdown();
        }
	}

	public abstract void receiveCard(String card);
	public abstract void executeTurn(String card, boolean exec);
	public abstract void shutdown();
}
