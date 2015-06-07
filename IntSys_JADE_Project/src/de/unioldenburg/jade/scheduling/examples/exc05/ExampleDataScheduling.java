package de.unioldenburg.jade.scheduling.examples.exc05;

import de.unioldenburg.jade.scheduling.constraints.Constraint;
import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Operation;
import de.unioldenburg.jade.scheduling.Product;
import de.unioldenburg.jade.scheduling.Resource;
import de.unioldenburg.jade.scheduling.ResourceTimePair;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.Variation;
import de.unioldenburg.jade.scheduling.constraints.AllJobsPlanned;
import de.unioldenburg.jade.scheduling.constraints.FulfilledDeadlines;
import de.unioldenburg.jade.scheduling.constraints.NoDoubleResourceAllocation;
import de.unioldenburg.jade.scheduling.constraints.ProductionOrderRespectedConstraint;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;
import de.unioldenburg.jade.scheduling.scheduler.Scheduler;
import de.unioldenburg.jade.scheduling.scheduler.SimpleFCFSScheduler;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class ExampleDataScheduling {
    
    private final static Resource m1 = new Resource("M1"),
            m2 = new Resource("M2"),
            m3 = new Resource("M3"),
            m4 = new Resource("M4");
    
    private static Product p1, p2, p3;
    
    static {
        // Create operations for P1
        Set<ResourceTimePair> operationResourceRequirements 
                = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m1, 2));
        Operation p1_o1 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m2, 2));
        Operation p1_o2 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m3, 3));
        Operation p1_o3 = new Operation(operationResourceRequirements);
        
        // Create variation for P1
        List<Operation> p1_v1_operations = new ArrayList<Operation>(1);
        p1_v1_operations.add(p1_o1);
        p1_v1_operations.add(p1_o2);
        p1_v1_operations.add(p1_o3);
        Variation p1_v1 = new Variation(p1_v1_operations);
        
        // Create P1
        Set<Variation> p1_variations = new HashSet<Variation>(1);
        p1_variations.add(p1_v1);
        p1 = new Product(p1_variations, "P1");
        
        // Create operations for P2
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m1, 3));
        Operation p2_o1 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m4, 2));
        Operation p2_o2 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m2, 3));
        Operation p2_o3 = new Operation(operationResourceRequirements);
        
        // Create variation for P2
        List<Operation> p2_v1_operations = new ArrayList<Operation>(1);
        p2_v1_operations.add(p2_o1);
        p2_v1_operations.add(p2_o2);
        p2_v1_operations.add(p2_o3);
        Variation p2_v1 = new Variation(p2_v1_operations);
        
        // Create P2
        Set<Variation> p2_variations = new HashSet<Variation>(1);
        p2_variations.add(p2_v1);
        p2 = new Product(p2_variations, "P2");
        
        // Create operations for P3
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m2, 1));
        Operation p3_o1 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m4, 2));
        Operation p3_o2 = new Operation(operationResourceRequirements);
        operationResourceRequirements = new HashSet<ResourceTimePair>(1);
        operationResourceRequirements.add(new ResourceTimePair(m3, 1));
        Operation p3_o3 = new Operation(operationResourceRequirements);
        
        // Create variation for P1
        List<Operation> p3_v1_operations = new ArrayList<Operation>(1);
        p3_v1_operations.add(p3_o1);
        p3_v1_operations.add(p3_o2);
        p3_v1_operations.add(p3_o3);
        Variation p3_v1 = new Variation(p3_v1_operations);
        
        // Create P1
        Set<Variation> p3_variations = new HashSet<Variation>(1);
        p3_variations.add(p3_v1);
        p3 = new Product(p3_variations, "P3");
    }            
    
    public static void main(String[] args) {
        System.out.println("Creating resources for example data for excercise 05...");
        Set<Resource> resources = getResources();
        for (Resource r : resources) {
            System.out.println(r);
        }
        
        System.out.println("\nCreating products for example data for excercise 05...");
        Set<Product> products = getProducts();
        for (Product p : products) {
            System.out.println(p);
        }
        
        System.out.println("\nCreating jobs for example data for excercise 05...");
        List<Job> jobs = getJobs();
        for (Job j : jobs) {
            System.out.println(j);
        }
        
        System.out.println("\nLoading constraints");
        Set<Constraint> hardConstraints = new HashSet<Constraint>(3);
        hardConstraints.add(new AllJobsPlanned());
        hardConstraints.add(new NoDoubleResourceAllocation());
        hardConstraints.add(new ProductionOrderRespectedConstraint());
        Set<Constraint> softConstraints = new HashSet<Constraint>(1);
        softConstraints.add(new FulfilledDeadlines());
        
        System.out.println("Creating Schedule for planning...");
        ProcessPlanningProblem p = new ProcessPlanningProblem(jobs, products, 
                resources, hardConstraints, softConstraints);
        Scheduler s = new SimpleFCFSScheduler();
        Schedule schedule = s.createSchedule(p);
        if (schedule != null) {
            System.out.println("\nDONE: Final schedule created:");
            schedule.printSchedule();
        } else {
            System.out.println("DONE: No schedule was created");
        }
    }
    
    public static Set<Resource> getResources() {
        Set<Resource> resources = new HashSet<Resource>(4);
        resources.add(m1);
        resources.add(m2);
        resources.add(m3);
        resources.add(m4);
        return resources;
    }
    
    public static Set<Product> getProducts() {
        HashSet<Product> products = new HashSet<Product>(3);
        products.add(p1);
        products.add(p2);
        products.add(p3);
        return products;
    }
    
    public static List<Job> getJobs() {
        ArrayList<Job> jobs = new ArrayList<Job>(10);
        jobs.add(new Job("A1", p1, 3, 0, 12));
        jobs.add(new Job("A2", p1, 1, 0, 27));
        jobs.add(new Job("A3", p2, 1, 0, 132));
        jobs.add(new Job("A4", p3, 1, 16, 52));
        jobs.add(new Job("A5", p2, 1, 17, 123));
        jobs.add(new Job("A6", p2, 1, 27, 1024));
        jobs.add(new Job("A7", p3, 1, 18, 27));
        jobs.add(new Job("A8", p3, 1, 0, 12));
        jobs.add(new Job("A9", p1, 1, 17, 147));
        jobs.add(new Job("A10", p2, 7, 12, 2048));
        return jobs;
    }
    
    
}
