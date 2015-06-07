package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;

/**
 *
 * @author Christoph Küpker
 */
public interface Scheduler {
    
    public Schedule createSchedule(ProcessPlanningProblem s);
}
