package de.unioldenburg.jade.processscheduling.dataStructure;

import java.util.ArrayList;

/**
 * The Operation of the product.
 * @author Armin Pistoor
 */
public class Operation {
	
	/**
	 * Operation name.
	 */
	private String operationName;
	
	/**
	 * Ressources.
	 */
	private ArrayList<Ressource> ressources;
	
	/**
	 * Constructor.
	 * @param operationName - the operationName.
	 */
	private Operation(String operationName, ArrayList<Ressource> ressources) {
		this.operationName = operationName;
		this.ressources = ressources;
	}
	
	public String getOperationName() {
		return this.operationName;
	}
	
	public ArrayList<Ressource> getRessources() {
		return this.ressources;
	}
	
	/**
	 * Get OperationSet 1.
	 * @return the operationSet.
	 */
	public final static ArrayList<Operation> GET_OPERATION_SET_1() {
		ArrayList<Operation> operationSet = new ArrayList<Operation>();
		operationSet.add(new Operation("1", Ressource.GET_Ressource_SET_1()));
		operationSet.add(new Operation("2", Ressource.GET_Ressource_SET_1()));
		operationSet.add(new Operation("3", Ressource.GET_Ressource_SET_1()));
		return operationSet;
	}

}
