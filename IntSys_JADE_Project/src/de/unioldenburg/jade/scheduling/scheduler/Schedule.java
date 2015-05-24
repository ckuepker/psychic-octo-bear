package de.unioldenburg.jade.scheduling.scheduler;

import java.util.List;

/**
 * Result of a Scheduler resulting in a set of ResourceAllocationPlans and information
 * on end times of jobs.
 * @author Christoph Küpker
 */
public class Schedule {
    
    private List<ResourceAllocationPlan> resourceAllocations;
    private List<PlannedJob> plannedJobs;

    public Schedule(List<ResourceAllocationPlan> resourceAllocations, List<PlannedJob> plannedJobs) {
        this.resourceAllocations = resourceAllocations;
        this.plannedJobs = plannedJobs;
    }
    
    public void printSchedule() {
        int maxTime = 0;
        for (ResourceAllocationPlan allocation : resourceAllocations) {
            System.out.println(allocation);
            if (allocation.getTime() > maxTime) {
                maxTime = allocation.getTime();
            }
        }
        System.out.print("t:  |");
        for (int i = 0; i < maxTime; i++) {
            if (i < 10) {
                System.out.print(i + "  |");
            } else if (i < 100) {
                System.out.print(i + " |");
            }
        }
        System.out.print("\n\n");
    }

}
