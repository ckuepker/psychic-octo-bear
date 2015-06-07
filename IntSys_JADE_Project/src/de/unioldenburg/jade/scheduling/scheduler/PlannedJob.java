package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Job;
import de.unioldenburg.jade.scheduling.Variation;

/**
 * Representation of a Job which was planned in scheduling, basically a Job 
 * extended by the planned completion time.
 * @author Christoph Küpker
 */
public class PlannedJob {

    private Job job;
    private int completionTime;
    private Variation variation;

    public PlannedJob(Job job, int completionTime, Variation variant) {
        this.job = job;
        this.completionTime = completionTime;
        this.variation = variant;
    }

    public Job getJob() {
        return job;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public Variation getVariation() {
        return variation;
    }
}
