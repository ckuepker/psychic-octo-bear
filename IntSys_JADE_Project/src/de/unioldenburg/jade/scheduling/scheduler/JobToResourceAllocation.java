package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Operation;
import de.unioldenburg.jade.scheduling.Variation;

/**
 *
 * @author Christoph Küpker
 */
public class JobToResourceAllocation {
    
    private Job job;
    private int duration,
            starttime;
    private Variation variation;
    private Operation operation;

    public JobToResourceAllocation(Job job, Variation variation, 
            Operation operation, int duration, int starttime) {
        this.job = job;
        this.duration = duration;
        this.starttime = starttime;
        this.variation = variation;
        this.operation = operation;
    }

    public Job getJob() {
        return job;
    }

    public int getDuration() {
        return duration;
    }
    
    public int getStarttime() {
        return starttime;
    }

    public Variation getVariation() {
        return variation;
    }

    public Operation getOperation() {
        return operation;
    }
}
