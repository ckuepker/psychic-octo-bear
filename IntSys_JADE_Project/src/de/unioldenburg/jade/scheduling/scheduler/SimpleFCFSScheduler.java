package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Operation;
import de.unioldenburg.jade.scheduling.Product;
import de.unioldenburg.jade.scheduling.Resource;
import de.unioldenburg.jade.scheduling.ResourceTimePair;
import de.unioldenburg.jade.scheduling.Schedule;
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
    
    private Map<Resource, ResourceAllocationPlan> plans 
            = new HashMap<Resource, ResourceAllocationPlan>();

    @Override
    public List<ResourceAllocationPlan> schedule(Schedule s) {
        Set<Resource> resources = s.getResources();
        for (Resource r : resources) {
            this.plans.put(r, new ResourceAllocationPlan(r));
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
                        +" starting at "+releaseTime+" running for "+duration);
                this.plans.get(r).append(j, duration, releaseTime);
                releaseTime = this.plans.get(r).getTime();
            }
        }
        return new ArrayList<ResourceAllocationPlan>(this.plans.values());
    }
    
}
