package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;

/**
 * Representation of a Job which was planned in scheduling, basically a Job 
 * extended by the planned completion time.
 * @author Christoph Küpker
 */
public class PlannedJob {

    private Job job;
    private int completionTime;

    public PlannedJob(Job job, int completionTime) {
        this.job = job;
        this.completionTime = completionTime;
    }

    public Job getJob() {
        return job;
    }

    public int getCompletionTime() {
        return completionTime;
    }
    
    
}
