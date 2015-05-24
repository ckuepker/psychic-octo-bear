package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Resource;
import java.util.ArrayList;
import java.util.List;
import sun.nio.ch.Util;

/**
 *
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
    
    public void append(Job j, int duration, int releaseTime) {
        if (time < releaseTime) {
            System.out.println(resource.getName()+": Fast forwarding to "+releaseTime);
            time = releaseTime;
        } else if (time > releaseTime) {
            System.out.println(resource.getName()+": Postponing "+j.getIdentifier()
                    + " to "+time+" because machine is busy");
        }
        jobs.add(new JobToResourceAllocation(j, duration, time));
        time += duration;
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
            s += alloc.getJob().getIdentifier();
            if (alloc.getJob().getIdentifier().length() == 2) {
                s += "XX";
            } else if (alloc.getJob().getIdentifier().length() == 3) {
                s += "X";
            }
            for (int i = 0; i < alloc.getDuration() - 1; i++) {
                s += "XXXX";
            }
            time += alloc.getDuration();
        }        
        return s;
    }
}
