package de.unioldenburg.jade.scheduling.examples.exc05.constraints;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.scheduler.PlannedJob;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;

/**
 * Constraint which checks whether the requested for all jobs was fulfilled.
 * @author Christoph Küpker
 */
public class FulfilledDeadlines extends Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem problem, Schedule schedule) {
        for (PlannedJob job : schedule.getPlannedJobs()) {
            if (job.getCompletionTime() > job.getJob().getEndDate()) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "The requested deadline for any job has to be fulfilled.";
    }

    
}
