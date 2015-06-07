package de.unioldenburg.jade.scheduling.constraints;

import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;

/**
 *
 * @author Christoph Küpker
 */
public class AllJobsPlanned extends Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem problem, Schedule schedule) {
        return problem.getJobs().size() == schedule.getPlannedJobs().size();
    }

    @Override
    public String getDescription() {
        return "All jobs planned";
    }
    
}
