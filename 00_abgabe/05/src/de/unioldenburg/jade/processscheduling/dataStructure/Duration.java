package de.unioldenburg.jade.processscheduling.dataStructure;

/**
 * The duration time for an order.
 * @author Armin Pistoor
 */
public class Duration {
	
	/**
	 * The duration time.
	 */
	private int time;
	
	/**
	 * Constructor.
	 * @param time - the duration time
	 */
	public Duration (int time) {
		this.time = time;
	}
	
	public int getTime() {
		return this.time;
	}

}
