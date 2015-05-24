package de.unioldenburg.jade.scheduling.examples.exc05.constraints;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;

/**
 *
 * @author Christoph Küpker
 */
public class AllJobsPlanned extends Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem problem, Schedule schedule) {
        return true;
    }

    @Override
    public String getDescription() {
        return "All jobs planned";
    }
    
}
