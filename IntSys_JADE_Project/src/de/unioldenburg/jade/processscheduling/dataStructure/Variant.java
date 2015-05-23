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
	private String variantName;
	
	/**
	 * List of operations to perform.
	 */
	private ArrayList<Operation> operations;
	
	/**
	 * Constructor.
	 * @param variantName - the name of the variant.
	 * @param operations - the operationSet.
	 */
	private Variant(String variantName, ArrayList<Operation> operations) {
		this.variantName = variantName;
		this.operations = operations;
	}
	
	public String getVariantName() {
		return this.variantName;
	}
	
	public ArrayList<Operation> getOperations() {
		return this.operations;
	}
	
	/**
	 * Get VariantSet 1.
	 * @return the variantSet.
	 */
	public final static ArrayList<Variant> GET_VARIANT_SET_1() {
		ArrayList<Variant> variantSet = new ArrayList<Variant>();
		variantSet.add(new Variant("1", Operation.GET_OPERATION_SET_1()));
		return variantSet;
	}

}
