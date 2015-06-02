package de.unioldenburg.jade.scheduling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Reads a JSON-file and returns a processPlanningProblem
 * @author Armin Pistoor
 *
 */
public class ProcessPlanningProblemLoader {

	private static final String JSON_FILE = "resources\\bigbeispieldata.json";

	public static ProcessPlanningProblem getJsonResource(
			Set<Constraint> hardConstraints, Set<Constraint> softConstraints) {
		JsonReader jsonReader;
		ProcessPlanningProblem processPlanningProblem = null;

		try {
			FileReader fr = new FileReader(JSON_FILE);
			jsonReader = Json.createReader(fr);
			JsonObject jsonObj = jsonReader.readObject();
			jsonReader.close();
			fr.close();
			JsonArray jsonOrders = jsonObj.getJsonArray("orders");
			JsonArray jsonProducts = jsonObj.getJsonArray("products");
			JsonArray jsonResources = jsonObj.getJsonArray("resources");
			List<Job> orders = new ArrayList<Job>();
			Set<Product> products = new HashSet<Product>();
			Set<Resource> resources = new HashSet<Resource>();

			// Get resources
			for (int i = 0; i < jsonResources.size(); i++) {
				resources.add(new Resource(jsonResources.getString(i)));
			}

			// Get products
			for (int i = 0; i < jsonProducts.size(); i++) {
				JsonObject jsonProduct = jsonProducts.getJsonObject(i);

				// Get Variants
				JsonArray jsonVariants = jsonProduct.getJsonArray("variants");
				Set<Variation> variants = new HashSet<Variation>();
				for (int j = 0; j < jsonVariants.size(); j++) {
					JsonArray jsonOperations = jsonVariants.getJsonArray(j);
					List<Operation> operations = new ArrayList<Operation>();

					// Get operations/resourceTimePairs
					Set<ResourceTimePair> resourceTimePairs = new HashSet<ResourceTimePair>();
					for (int k = 0; k < jsonOperations.size(); k++) {
						JsonObject jsonOperation = jsonOperations
								.getJsonObject(k);
						ResourceTimePair resourceTimePair = null;

						// Get resource time pair
						for (Resource resource : resources) {
							if (resource.getName().equals(
									(jsonOperation.getString("resource")))) {
								resourceTimePair = new ResourceTimePair(
										resource,
										jsonOperation.getInt("duration"));
								resourceTimePairs.add(resourceTimePair);
							}
						}
						operations.add(new Operation(resourceTimePairs, jsonOperation.getInt("index")));
					}
					variants.add(new Variation(operations));
				}
				products.add(new Product(variants, jsonProduct
						.getString("name")));
			}

			// Get orders
			for (int i = 0; i < jsonOrders.size(); i++) {
				JsonObject order = jsonOrders.getJsonObject(i);
				for (Product product : products) {
					if (product.getName().equals(order.getString("product"))) {
						orders.add(new Job(order.getInt("id"), product, order
								.getInt("amount"), order.getInt("start"), order
								.getInt("end"), order.getInt("priority")));
					}
				}
			}

			//Create processPLanningProblem
			processPlanningProblem = new ProcessPlanningProblem(orders,
					products, resources, hardConstraints, softConstraints);

			// Zur Überprüfung, dass die richtigen Daten eingelesen werden ;-)
//			for (Product product : products) {
//				System.out.print(product.getName());
//				System.out.print(" Anzahl Varianten: "
//						+ product.getVariations().size());
//				for (Variation variation : product.getVariations()) {
//					System.out.print(" Anzahl Operationen: "
//							+ variation.getOperations().size());
//				}
//				System.out.println();
//			}
//			for (Job job : orders) {
//				System.out.print("   Jobid: " + job.getIdentifier()
//						+ "  Produkt: " + job.getProduct().getName());
//			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return processPlanningProblem;
	}
}
