package de.unioldenburg.jade.processscheduling.main;

import de.unioldenburg.jade.processscheduling.dataStructure.Schedule;

/**
 * The main class.
 * @author Armin Pistoor
 */
public class Main {
	
	/**
	 * The Main method.
	 * @param args - args
	 */
	public static void main(String[] args) {
		Schedule defaultSchedule = new Schedule(Schedule.SCHEDULE_FORM_DEFAULT);
		try {
			defaultSchedule.printDefaultSchedule();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
