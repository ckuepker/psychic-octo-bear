package de.unioldenburg.jade.scheduling;

/**
 *
 * @author Christoph Küpker
 */
public class ResourceTimePair {

    private Resource resource;
    private int time;

    public ResourceTimePair(Resource resource, int time) {
        this.resource = resource;
        this.time = time;
    }

    public Resource getResource() {
        return resource;
    }

    public int getTime() {
        return time;
    }
    
    @Override
    public String toString() {
        return "\""+getResource().getName()+"\", "+getTime();
    }
}
