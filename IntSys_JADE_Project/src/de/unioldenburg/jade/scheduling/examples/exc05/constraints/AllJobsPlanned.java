package de.unioldenburg.jade.scheduling.examples.exc05.constraints;

import de.unioldenburg.jade.scheduling.Constraint;
import de.unioldenburg.jade.scheduling.ProcessPlanningProblem;

/**
 *
 * @author Christoph Küpker
 */
public class AllJobsPlanned implements Constraint {

    @Override
    public boolean isValid(ProcessPlanningProblem s) {
        return false;
    }
    
}
