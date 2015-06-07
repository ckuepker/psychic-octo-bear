package de.unioldenburg.jade.scheduling.constraints;

import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;
import de.unioldenburg.jade.scheduling.scheduler.Schedule;

/**
 *
 * @author Christoph Küpker
 */
public abstract class Constraint {
    
    /**
     * Checks whether the given problem and schedule, this constraint remains
     * unviolated.
     * @return true iff the constraint is unviolated
     */
    public abstract boolean isValid(ProcessPlanningProblem problem, Schedule schedule);
    
    /**
     * Gives a natural language description of this constraint which should even
     * be understandable by the end user.
     * @return 
     */
    public abstract String getDescription();
}
