package de.unioldenburg.jade.scheduling;

/**
 *
 * @author Christoph Küpker
 */
public interface Constraint {
    
    public boolean isValid(ProcessPlanningProblem s);
}
