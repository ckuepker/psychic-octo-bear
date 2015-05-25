package de.unioldenburg.jade.scheduling;

import de.unioldenburg.jade.scheduling.scheduler.ResourceAllocationPlan;
import de.unioldenburg.jade.scheduling.scheduler.Scheduler;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class ProcessPlanningProblem {
    
    private List<Job> jobs;
    private Set<Product> products;
    private Set<Resource> resources;
    private Set<Constraint> hardConstraints;
    private Set<Constraint> softConstraints;

    public ProcessPlanningProblem(List<Job> jobs, Set<Product> products, 
            Set<Resource> resources, Set<Constraint> hardConstraints, 
            Set<Constraint> softConstraints) {
        this.jobs = jobs;
        this.products = products;
        this.resources = resources;
        this.hardConstraints = hardConstraints;
        this.softConstraints = softConstraints;
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
}
