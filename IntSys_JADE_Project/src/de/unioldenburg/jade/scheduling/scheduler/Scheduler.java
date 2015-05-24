package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import java.util.List;

/**
 *
 * @author Christoph Küpker
 */
public interface Scheduler {
    
    public Schedule createSchedule(ProcessPlanningProblem s);
}
