package de.unioldenburg.jade.scheduling;

import java.util.Set;

/**
 *
 * @author Christoph Küpker
 */
public class Product {
    
    private Set<Variation> variations;
    private String name;

    public Product(Set<Variation> variations, String name) {
        this.variations = variations;
        this.name = name;
    }

    public Set<Variation> getVariations() {
        return variations;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "Product("+getName()+")\n"+getVariations().size()+" variation(s) "
                + "available\n";
        for (Variation v : getVariations()) {
            s += "\tVariation: "+v.getOperations().size()+" operations ([resource, duration]):\n";
            for (Operation o : v.getOperations()) {
                s += "\t\tOperation("+o.getResources().toString()+")\n";
            }            
        }
        return s;
    }
}
