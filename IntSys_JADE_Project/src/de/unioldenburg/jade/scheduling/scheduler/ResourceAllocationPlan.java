package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Allocates sequences Job(operations) to resources and tracks time of a resource
 * @author Christoph Küpker
 */
public class ResourceAllocationPlan {
    
    private List<JobToResourceAllocation> jobs = new ArrayList<JobToResourceAllocation>();
    private Resource resource;
    private int time = 0;
    private final static String WAITING_SECOND_STRING = "|   ";

    public ResourceAllocationPlan(Resource resource) {
        this.resource = resource;
    }
    
    /**
     * Appends the given Job (well actually an operation of the job but we don't
     * care about Operations here) to the resource of this allocation plan, 
     * not before release time and taking duration time on the resource. 
     * @param job The job
     * @param duration Time the Job requires on the ressource
     * @param releaseTime Earliest time to start the job on this resource
     * @return Time after the job is completed on this resource 
     * ([postponement+]releaseTime+duration)
     */
    public int append(Job job, int duration, int releaseTime) {
        if (time < releaseTime) {
            System.out.println(resource.getName()+": Fast forwarding to "+releaseTime);
            time = releaseTime;
        } else if (time > releaseTime) {
            System.out.println(resource.getName()+": Postponing "+job.getIdentifier()
                    + " to "+time+" because machine is busy");
        }
        jobs.add(new JobToResourceAllocation(job, duration, time));
        time += duration;
        return time;
    }

    public Resource getResource() {
        return resource;
    }

    public int getTime() {
        return time;
    }
    
    @Override
    public String toString() {
        String s = resource.getName()+": ";
        int time = 0;
        for (JobToResourceAllocation alloc : this.jobs) {
            while (time < alloc.getStartTime()) {
                s += WAITING_SECOND_STRING;
                time++;
            }
            int nameStringLength = 0;
            String identifier = alloc.getJob().getIdentifier();
            s += identifier;
            nameStringLength += identifier.length();
            if (alloc.getJob().getAmount() > 1) {
                s += "*"+alloc.getJob().getAmount();
                nameStringLength += 2;
            }
            if (nameStringLength == 2) {
                s += "XX";
            } else if (nameStringLength == 3) {
                s += "X";
            }
            for (int i = 0; i < alloc.getDuration() - 1; i++) {
                s += "XXXX";
            }
            time += alloc.getDuration();
        }        
        return s;
    }

    public List<JobToResourceAllocation> getJobs() {
        return jobs;
    }
    
    
}
