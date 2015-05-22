package de.unioldenburg.jade.processscheduling.dataStructure;

import java.util.ArrayList;

/**
 * The ressource needed for an operation.
 * @author Armin Pistoor
 *
 */
public class Ressource {
	
	/**
	 * Operation name.
	 */
	private String ressourceName;
	
	/**
	 * The output string.
	 */
	private String outputString;
	
	/**
	 * Constructor.
	 * @param ressourceName - the ressourceName.
	 */
	private Ressource(String ressourceName) {
		this.ressourceName = ressourceName;
		this.outputString = "";
	}
	
	public String getRessourceName() {
		return this.ressourceName;
	}
	
	/**
	 * Get RessourceSet 1.
	 * @return the operationSet.
	 */
	public final static ArrayList<Ressource> GET_Ressource_SET_1() {
		ArrayList<Ressource> ressourceSet = new ArrayList<Ressource>();
		ressourceSet.add(new Ressource("M1"));
		ressourceSet.add(new Ressource("M2"));
		ressourceSet.add(new Ressource("M3"));
		ressourceSet.add(new Ressource("M4"));
		return ressourceSet;
	}

	public String getOutputString() {
		return this.ressourceName + ":" + this.outputString;
	}

	public void addStringToOutputString(String addString, int start, int timeNeeded) {
		int spacesNeeded = timeNeeded * 6; //One time unit equals 6 spaces -> example: <|P01| >
		start = start * 6;
		if (this.outputString.length() <= start) {
			String emptyString = "";
			for (int i = 0; i < (start + spacesNeeded) ; i++) {
				emptyString = emptyString + " ";
			}
			this.outputString = this.outputString + emptyString;
			this.outputString = this.outputString.substring(0, start);
			this.outputString = this.outputString +  " |";
			for (int i = 0; i < timeNeeded - 1; i++) {
				this.outputString = outputString + "___";
			}
			this.outputString = this.outputString + addString;
			for (int i = 0; i < timeNeeded - 1; i++) {
				this.outputString = outputString + "___";
			}
			this.outputString = this.outputString +  "|";
		} else {
			this.outputString = this.outputString +  " |";
			for (int i = 0; i < timeNeeded - 1; i++) {
				this.outputString = outputString + "___";
			}
			this.outputString = this.outputString + addString;
			for (int i = 0; i < timeNeeded - 1; i++) {
				this.outputString = outputString + "___";
			}
			this.outputString = this.outputString +  "|";
		}
	}

}
