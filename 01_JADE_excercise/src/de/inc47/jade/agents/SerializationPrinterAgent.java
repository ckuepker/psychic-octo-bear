package de.inc47.jade.agents;

import jade.core.Agent;

/**
 * Agent that prints the given serialized arguments in a structured manner.
 * @author ckuepker
 */
public class SerializationPrinterAgent extends Agent {
	
	protected void setup() {
		System.out.println("Hello this is Agent "+getAID().getName()+" looking for printable data!");
		
		Object[] args = getArguments();
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				System.out.println("argument #"+i+": "+(String) args[i]);
			}
		}
		System.out.println("All arguemnts printed");
		doDelete();
	}
	
	
	
	protected void takeDown() {
		// TODO Auto-generated method stub
		super.takeDown();
	}
}
