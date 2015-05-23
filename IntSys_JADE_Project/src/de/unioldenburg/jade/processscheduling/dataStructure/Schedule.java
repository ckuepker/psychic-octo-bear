package de.unioldenburg.jade.processscheduling.dataStructure;

import java.util.ArrayList;

/**
 * The schedule to produce all products.
 * @author Armin Pistoor
 *
 */
public class Schedule {
	
	/**
	 * The different schedule forms
	 */
	public final static int SCHEDULE_FORM_DEFAULT = 1,
							SCHEDULE_FORM_OPTIMIZED = 2;
							//weitere Pläne...
	
	/**
	 * The products to produce.
	 */
	private ArrayList<Product> products;
	
	/**
	 * Constructor.
	 * @param scheduleForm - the schedule to use
	 */
	public Schedule(int scheduleForm) {
		switch (scheduleForm) {
		case 1:
			createDefaultSchedule();
			break;
			
		case 2:
			break;

		default:
			createDefaultSchedule();
			break;
		}
		
	}

	/**
	 * Creates the default schedule. 
	 */
	private void createDefaultSchedule () {
		this.products = Product.GET_PRODUCT_SET_1();
	}
	
	/**
	 * Shows the schedule on the console without an intelligent algorithm.
	 * @throws Exception 
	 */
	public void printDefaultSchedule() throws Exception {
		ArrayList<Ressource> ressources = this.collectRessources();
		for (Ressource ressource : ressources) {
			this.findPathUsingRessource(ressource);
		}
		int maxTime = 0;
		for (Ressource ressource : ressources) {
			System.out.println(ressource.getOutputString());
			if(ressource.getOutputString().length() > maxTime) {
				maxTime = ressource.getOutputString().length();
			}
		}
		StringBuilder timeline = new StringBuilder();
		timeline.append("T:  ");
		// -3 because of the Tag 'M1:'
		// /6 because 1 time unit equals 6 spaces
		for (int i = 1; i <= ((maxTime - 3) / 6); i++) {
			if (i < 10) {
				timeline.append("  " + i + "   ");
			} else if (i < 100) {
				timeline.append("  " + i + "  ");
			} else {
				throw new Exception("Production process is too long! Please customize the timeline!");
			}
		}
		System.out.println(timeline.toString());
	}
	
	/**
	 * Finds paths using the ressource.
	 * @param ressource - the ressource
	 * @return - the paths.
	 */
	private void findPathUsingRessource(Ressource currentRessource) {
		for (Product product : this.products) {
			Variant variety = product.getVariants().get(0);
			for (int i = 0; i < variety.getOperations().size(); i++) {
				Operation operation = variety.getOperations().get(i);
				Ressource ressource = operation.getRessources().get(i);
				if (ressource.getRessourceName().equals(currentRessource.getRessourceName())) {
//					int timeNeeded = 2;
//					int timeNeeded = (int)(Math.random() * 3 + 1);
					int timeNeeded = (int)(product.getComplexity()*ressource.getRunningTime()*operation.getExecutionTime());
					currentRessource.addStringToOutputString(product.getProductName()
							, product.getTime(), timeNeeded);
					product.addTime(timeNeeded);
				}
			}
		}		
	}

	/**
	 * Searches all used ressources.
	 * @return - the ressources
	 */
	private ArrayList<Ressource> collectRessources() {
		ArrayList<Ressource> ressources = new ArrayList<Ressource>();
		ArrayList<String> ressourceNames = new ArrayList<String>();
		for (Product product : this.products) {
			for (Variant variety : product.getVariants()) {
				for (Operation operation : variety.getOperations()) {
					for (Ressource ressource : operation.getRessources()) {
						if (!ressourceNames.contains(ressource.getRessourceName())) {
							ressources.add(ressource);
							ressourceNames.add(ressource.getRessourceName());
						}
					}
				}
			}
		}
		return ressources;
	}

	/**
	 * Shows the schedule on the console with an intelligent algorithm.
	 */
	public void printOptimizedSchedule() {
		//intelligent method
	}

}
