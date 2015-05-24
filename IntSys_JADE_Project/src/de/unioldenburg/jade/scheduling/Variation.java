package de.unioldenburg.jade.scheduling;

import java.util.List;

/**
 *
 * @author Christoph Küpker
 */
public class Variation {
    
    private List<Operation> operations;

    public Variation(List<Operation> operations) {
        this.operations = operations;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
