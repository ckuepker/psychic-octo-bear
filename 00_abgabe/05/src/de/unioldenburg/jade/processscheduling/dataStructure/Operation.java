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
	 * Time to execute the operation
	 */
	private double executionTime;
	
	/**
	 * Constructor.
	 * @param operationName - the operationName
	 * @param ressources - the ressources
	 */
	private Operation(String operationName, ArrayList<Ressource> ressources) {
		this.operationName = operationName;
		this.ressources = ressources;
		this.executionTime = 1.0;
	}
	
	/**
	 * Constructor.
	 * @param operationName - the operationName
	 * @param ressources - the ressources
	 */
	private Operation(String operationName, ArrayList<Ressource> ressources, double executionTime) {
		this.operationName = operationName;
		this.ressources = ressources;
		this.executionTime = executionTime;
	}
	
	public String getOperationName() {
		return this.operationName;
	}
	
	public ArrayList<Ressource> getRessources() {
		return this.ressources;
	}
	
	public double getExecutionTime() {
		return this.executionTime;
	}
	
	/**
	 * Get OperationSet 1.
	 * @return the operationSet.
	 */
	public final static ArrayList<Operation> GET_OPERATION_SET_1() {
		ArrayList<Operation> operationSet = new ArrayList<Operation>();
		operationSet.add(new Operation("1", Ressource.GET_Ressource_SET_1(), 1.2));
		operationSet.add(new Operation("2", Ressource.GET_Ressource_SET_1(), 1.5));
		operationSet.add(new Operation("3", Ressource.GET_Ressource_SET_1(), 1.3));
		return operationSet;
	}

}
