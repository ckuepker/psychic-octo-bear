package de.unioldenburg.jade.scheduling.constraints;

import de.unioldenburg.jade.scheduling.Operation;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.Resource;
import de.unioldenburg.jade.scheduling.ResourceTimePair;
import de.unioldenburg.jade.scheduling.Variation;
import de.unioldenburg.jade.scheduling.scheduler.JobToResourceAllocation;
import de.unioldenburg.jade.scheduling.scheduler.PlannedJob;
import de.unioldenburg.jade.scheduling.scheduler.ResourceAllocationPlan;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;
import java.util.HashMap;
import java.util.Map;

/**
 * Constraint to check whether the operations are executed in correct order
 * for the used product-variation-combo in the schedule.
 * @author Christoph Küpker
 */
public class ProductionOrderRespectedConstraint extends Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem problem, Schedule schedule) {
        for (PlannedJob job : schedule.getPlannedJobs()) {
            Variation variation = job.getVariation();
            int lastOperationFinishTime = 0;
            for (Operation operation : variation.getOperations()) {
                for (ResourceTimePair resourceDuration : operation.getResources()) {
                    ResourceAllocationPlan plan = schedule.getResourceAllocation
                            (resourceDuration.getResource());
                    for (JobToResourceAllocation allocation : plan.getJobs()) {
                        if (allocation.getOperation().equals(operation) && allocation.getJob().equals(job.getJob())) {
                            if (allocation.getStarttime() < lastOperationFinishTime) {
                                return false;
                            }
                            lastOperationFinishTime = allocation.getStarttime()+allocation.getDuration();
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "The production order has to be respected for every product.";
    }    
}
