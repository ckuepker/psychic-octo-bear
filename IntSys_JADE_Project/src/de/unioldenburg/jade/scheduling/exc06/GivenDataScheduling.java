package de.unioldenburg.jade.scheduling.exc06;

import java.util.HashSet;
import java.util.Set;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblemLoader;
import de.unioldenburg.jade.scheduling.examples.exc05.constraints.AllJobsPlanned;
import de.unioldenburg.jade.scheduling.examples.exc05.constraints.FulfilledDeadlines;
import de.unioldenburg.jade.scheduling.examples.exc05.constraints.NoDoubleResourceAllocation;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;
import de.unioldenburg.jade.scheduling.scheduler.Scheduler;
import de.unioldenburg.jade.scheduling.scheduler.SimpleFCFSScheduler;

/**
 * @author Christoph Küpker, Armin Pistoor
 */
public class GivenDataScheduling {

    public static void main(String[] args) {
        System.out.println("\nLoading constraints");
        Set<Constraint> hardConstraints = new HashSet<Constraint>(3);
        hardConstraints.add(new AllJobsPlanned());
        hardConstraints.add(new NoDoubleResourceAllocation());
        Set<Constraint> softConstraints = new HashSet<Constraint>(1);
        softConstraints.add(new FulfilledDeadlines());
        
        System.out.println("Creating Schedule for planning...");
        ProcessPlanningProblem p = ProcessPlanningProblemLoader.getJsonResource(hardConstraints, softConstraints);
        Scheduler s = new SimpleFCFSScheduler();
        
        Schedule schedule = s.createSchedule(p);
        System.out.println("\nFinal schedule created:");
        if (schedule != null) {
            schedule.printSchedule();
        } else {
            System.out.println("No schedule was created");
        }
    }
}
