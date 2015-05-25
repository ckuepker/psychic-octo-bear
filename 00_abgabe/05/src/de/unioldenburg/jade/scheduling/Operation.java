package de.unioldenburg.jade.scheduling;

import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class Operation {
    
    private Set<ResourceTimePair> resources;

    public Operation(Set<ResourceTimePair> resources) {
        this.resources = resources;
    }

    public Set<ResourceTimePair> getResources() {
        return resources;
    }
}
