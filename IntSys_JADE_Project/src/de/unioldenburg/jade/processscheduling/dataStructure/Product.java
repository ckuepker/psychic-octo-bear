package de.unioldenburg.jade.processscheduling.dataStructure;

import java.util.ArrayList;

/**
 * The product - synonym to order.
 * @author Armin Pistoor
 *
 */
public class Product {
	
	/**
	 * Product name.
	 */
	private String productName;
	
	/**
	 * List of different varieties.
	 */
	private ArrayList<Variant> varieties;
	
	/**
	 * The time the product already used.
	 */
	private int time;
	
	/**
	 * The complexity of the product. Needed to calculate the time needed to produce the product.
	 */
	private double complexity;
	
	/**
	 * Constructor.
	 * @param productName - the productName.
	 * @param varieties - the varietySet.
	 */
	public Product(String productName, ArrayList<Variant> varieties, double complexity) {
		this.productName = productName;
		this.varieties = varieties;
		this.complexity = complexity;
		this.time = 0;
	}
	
	/**
	 * adds time to the used time;
	 */
	public void addTime(int time) {
		this.time = this.time + time;
	}
	
	public int getTime() {
		return this.time;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public ArrayList<Variant> getVarieties() {
		return this.varieties;
	}
	
	public double getComplexity() {
		return this.complexity;
	}
	
	/**
	 * Get productSet 1.
	 * @return the productSet.
	 */
	public final static ArrayList<Product> GET_PRODUCT_SET_1() {
		ArrayList<Product> productSet = new ArrayList<Product>();
		productSet.add(new Product("P01", Variant.GET_VARIETY_SET_1(), 1.0));
		productSet.add(new Product("P02", Variant.GET_VARIETY_SET_1(), 1.2));
		productSet.add(new Product("P03", Variant.GET_VARIETY_SET_1(), 1.5));
		productSet.add(new Product("P04", Variant.GET_VARIETY_SET_1(), 2.2));
		productSet.add(new Product("P05", Variant.GET_VARIETY_SET_1(), 2.0));
		productSet.add(new Product("P06", Variant.GET_VARIETY_SET_1(), 1.8));
		productSet.add(new Product("P07", Variant.GET_VARIETY_SET_1(), 1.4));
		productSet.add(new Product("P08", Variant.GET_VARIETY_SET_1(), 2.7));
		productSet.add(new Product("P09", Variant.GET_VARIETY_SET_1(), 2.4));
		productSet.add(new Product("P10", Variant.GET_VARIETY_SET_1(), 3.1));
		return productSet;
	}

}
