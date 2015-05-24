package de.unioldenburg.jade.scheduling;

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

    public Schedule(List<Job> jobs, Set<Product> products, 
            Set<Resource> resources, Set<Constraint> hardConstraints, 
            Set<Constraint> softConstraints) {
        this.jobs = jobs;
        this.products = products;
        this.resources = resources;
        this.hardConstraints = hardConstraints;
        this.softConstraints = softConstraints;
    }
    
    private void schedule() {
        
    }
    
    public void printSchedule() {
        
    }
    
    public void printConstraintValidationResult() {
        
    }
}
