package de.unioldenburg.jade.scheduling.scheduler;

import de.unioldenburg.jade.scheduling.Constraint;

/**
 *
 * @author Christoph Küpker
 */
public class HardConstraintViolatedException extends Exception {
    
    private Constraint violatedConstraint;

    public HardConstraintViolatedException(Constraint c) {
        this.violatedConstraint = c;
    }
    
    @Override
    public String getMessage() {
        return violatedConstraint.getDescription();
    }
}
