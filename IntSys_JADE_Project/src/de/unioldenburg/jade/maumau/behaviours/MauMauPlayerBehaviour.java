package de.unioldenburg.jade.maumau.behaviours;

import jade.lang.acl.ACLMessage;
import de.unioldenburg.jade.behaviours.WaitForMessageBehaviour;

/**
 * @author ckuepker
 * 
 */
public abstract class MauMauPlayerBehaviour extends WaitForMessageBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2932285930989468403L;
	
	public static final String DISTRIBUTE_CARD_MESSAGE_CONTENT = "distCard",
			NEXT_MESSAGE_CONTENT = "next",
			GAMEOVER_MESSAGE_CONTENT = "gameover",
			NEXT_EXECUTE_CARD_MESSAGE_CONTENT = "nextExec",
			NEXT_WISHED_CARD_MESSAGE_CONTENT = "wished",
			PLAYED_CARD_MESSAGE_CONTENT_PREFIX = "played";

	@Override
	public void handleMessage(ACLMessage msg) {
		if (msg.getContent().startsWith(DISTRIBUTE_CARD_MESSAGE_CONTENT)) {
			this.receiveCard(msg.getContent().substring(8));
        } else if (msg.getContent().startsWith(NEXT_EXECUTE_CARD_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(8), true);
        } else if (msg.getContent().startsWith(NEXT_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(4), false);
        } else if (msg.getContent().startsWith(NEXT_WISHED_CARD_MESSAGE_CONTENT)) {
            executeTurn(msg.getContent().substring(6) + "0", false);
        } else if (msg.getContent().equals(GAMEOVER_MESSAGE_CONTENT)) {
        	this.shutdown();
        }
	}

	public abstract void receiveCard(String card);
	public abstract void executeTurn(String card, boolean exec);
	public abstract void shutdown();
}
