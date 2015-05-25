package de.unioldenburg.jade.scheduling.examples.exc05.constraints;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.scheduler.JobToResourceAllocation;
import de.unioldenburg.jade.scheduling.scheduler.ResourceAllocationPlan;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;

/**
 * Constraint that checks whether no two jobs are allocated to the same resource
 * at the same time.
 * @author Christoph Küpker
 */
public class NoDoubleResourceAllocation extends Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem problem, Schedule schedule) {
        for (ResourceAllocationPlan allocation : schedule.getResourceAllocations()) {
            int allocationReleaseTime = 0;
            for (JobToResourceAllocation job : allocation.getJobs()) {
                if (job.getStartTime() < allocationReleaseTime) {
                    return false;
                }
                allocationReleaseTime = job.getStartTime() + job.getDuration();
            }
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "No machine has more than one associated jobs at any point in time.";
    }

}
