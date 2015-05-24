package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Operation;
import de.unioldenburg.jade.scheduling.Product;
import de.unioldenburg.jade.scheduling.Resource;
import de.unioldenburg.jade.scheduling.ResourceTimePair;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.Variation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class SimpleFCFSScheduler implements Scheduler {
    
    @Override
    public Schedule createSchedule(ProcessPlanningProblem s) {
        Map<Resource, ResourceAllocationPlan> plans 
                = new HashMap<Resource, ResourceAllocationPlan>();
        List<PlannedJob> completedJobs = new ArrayList<PlannedJob>();
        Set<Resource> resources = s.getResources();
        for (Resource r : resources) {
            plans.put(r, new ResourceAllocationPlan(r));
        }
        for (Job j : s.getJobs()) {
            int releaseTime = j.getStartDate();
            Product p = j.getProduct();
            Variation v = p.getVariations().iterator().next();
            for (Operation o : v.getOperations()) {
                ResourceTimePair requirement = o.getResources().iterator().next();
                Resource r = requirement.getResource();
                int duration = requirement.getTime();
                System.out.println("Putting job "+j.getIdentifier()+" on machine "+r.getName()
                        +" starting not before "+releaseTime+" running for "+duration);
                releaseTime = plans.get(r).append(j, duration, releaseTime);
            }
            // All operations planned. Add job as completed job to schedule
            completedJobs.add(new PlannedJob(j, releaseTime));
        }
        Schedule schedule = new Schedule(new ArrayList<ResourceAllocationPlan>(plans.values()), 
                completedJobs);
        try {
            this.validateConstraints(schedule, s);
        } catch (HardConstraintViolatedException ex) {
            System.out.println("FAILURE: The following hard constraint was "
                    + "violated:\t\n\t\""+ex.getMessage()+"\"\nNo Schedule will be "
                    + "created.");
            return null;
        }
        return schedule;
    }
    
    private void validateConstraints(Schedule schedule, ProcessPlanningProblem problem) 
            throws HardConstraintViolatedException {
        for (Constraint c : problem.getHardConstraints()) {
            if (!c.isValid(problem, schedule)) {
                throw new HardConstraintViolatedException(c);
            }
        }
        for (Constraint c : problem.getSoftConstraints()) {
            if (!c.isValid(problem, schedule)) {
                System.out.println("WARNING: The following soft constraint is "
                        + "being violated by the current schedule:\n\t"+c.getDescription());
            }
        }
    }
}
