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
	private ArrayList<Variety> varieties;
	
	/**
	 * The time the product already used.
	 */
	private int time;
	
	/**
	 * Constructor.
	 * @param productName - the productName.
	 * @param varieties - the varietySet.
	 */
	public Product(String productName, ArrayList<Variety> varieties) {
		this.productName = productName;
		this.varieties = varieties;
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
	
	public ArrayList<Variety> getVarieties() {
		return this.varieties;
	}
	
	/**
	 * Get productSet 1.
	 * @return the productSet.
	 */
	public final static ArrayList<Product> GET_PRODUCT_SET_1() {
		ArrayList<Product> productSet = new ArrayList<Product>();
		productSet.add(new Product("P01", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P02", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P03", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P04", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P05", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P06", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P07", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P08", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P09", Variety.GET_VARIETY_SET_1()));
		productSet.add(new Product("P10", Variety.GET_VARIETY_SET_1()));
		return productSet;
	}

}
