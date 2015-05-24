package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;

/**
 *
 * @author Christoph Küpker
 */
public class JobToResourceAllocation {
    
    private Job job;
    private int duration,
            starttime;

    public JobToResourceAllocation(Job job, int duration, int starttime) {
        this.job = job;
        this.duration = duration;
        this.starttime = starttime;
    }

    public Job getJob() {
        return job;
    }

    public int getDuration() {
        return duration;
    }
    
    public int getStartTime() {
        return starttime;
    }
}
