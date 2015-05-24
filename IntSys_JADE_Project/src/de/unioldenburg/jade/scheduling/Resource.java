package de.unioldenburg.jade.scheduling;

/**
 *
 * @author Christoph Küpker
 */
public class Resource {
    
    private String name;


    public String getName() {
        return name;
    }

    public Resource(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return "Resource(\""+getName()+"\")";
    }
}
