package de.unioldenburg.jade.processscheduling.dataStructure;

import java.util.ArrayList;

/**
 * The variety to produce the product.
 * @author Armin Pistoor
 */
public class Variant {
	
	/**
	 * Variant name.
	 */
	private String varietyName;
	
	/**
	 * List of operations to perform.
	 */
	private ArrayList<Operation> operations;
	
	/**
	 * Constructor.
	 * @param varietyName - the varietyName.
	 * @param operations - the operationSet.
	 */
	private Variant(String varietyName, ArrayList<Operation> operations) {
		this.varietyName = varietyName;
		this.operations = operations;
	}
	
	public String getVarietyName() {
		return this.varietyName;
	}
	
	public ArrayList<Operation> getOperations() {
		return this.operations;
	}
	
	/**
	 * Get VarietySet 1.
	 * @return the varietySet.
	 */
	public final static ArrayList<Variant> GET_VARIETY_SET_1() {
		ArrayList<Variant> varietySet = new ArrayList<Variant>();
		varietySet.add(new Variant("1", Operation.GET_OPERATION_SET_1()));
		return varietySet;
	}

}
