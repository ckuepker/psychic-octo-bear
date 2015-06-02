package de.unioldenburg.jade.scheduling;

import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class Operation {
    
    private Set<ResourceTimePair> resources;
    
    private int index;
    
    /**
     * Constructor for extra index for JSON-file.
     * @author Armin Pistoor
     */
    public Operation(Set<ResourceTimePair> resources, int index) {
        this.resources = resources;
        this.index = index;
    }

    public Operation(Set<ResourceTimePair> resources) {
        this.resources = resources;
    }

    public Set<ResourceTimePair> getResources() {
        return resources;
    }
    
    public int getIndex() {
    	return this.index;
    }
}
