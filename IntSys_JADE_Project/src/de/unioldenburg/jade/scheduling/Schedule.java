package de.unioldenburg.jade.scheduling;

import de.unioldenburg.jade.scheduling.scheduler.ResourceAllocationPlan;
import de.unioldenburg.jade.scheduling.scheduler.Scheduler;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class Schedule {
    
    private List<Job> jobs;
    private Set<Product> products;
    private Set<Resource> resources;
    private Set<Constraint> hardConstraints;
    private Set<Constraint> softConstraints;
    private Scheduler scheduler;

    public Schedule(List<Job> jobs, Set<Product> products, 
            Set<Resource> resources, Set<Constraint> hardConstraints, 
            Set<Constraint> softConstraints, Scheduler scheduler) {
        this.jobs = jobs;
        this.products = products;
        this.resources = resources;
        this.hardConstraints = hardConstraints;
        this.softConstraints = softConstraints;
        this.scheduler = scheduler;
    }
    
    private List<ResourceAllocationPlan> schedule() {
        return scheduler.schedule(this);
    }
    
    public void printSchedule() {
        List<ResourceAllocationPlan> plan = this.schedule();
        int maxTime = 0;
        for (ResourceAllocationPlan allocation : plan) {
            System.out.println(allocation);
            if (allocation.getTime() > maxTime) {
                maxTime = allocation.getTime();
            }
        }
        System.out.print("t:  |");
        for (int i = 0; i < maxTime; i++) {
            if (i < 10) {
                System.out.print(i+"  |");
            } else if (i < 100) {
                System.out.print(i+" |");
            }
        }
        System.out.print("\n\n");
    }
    
    public void printConstraintValidationResult() {
        
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public Set<Constraint> getHardConstraints() {
        return hardConstraints;
    }

    public Set<Constraint> getSoftConstraints() {
        return softConstraints;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
}
