package de.unioldenburg.jade.scheduling;

/**
 *
 * @author Christoph Küpker
 */
public class Job {
    
    private String identifier;
    private Product product;
    private int amount;
    private int startDate;
    private int endDate;

    public Job(String identifier, Product product, int amount, int startDate, int endDate) {
        this.identifier = identifier;
        this.product = product;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return endDate;
    }
    
    @Override
    public String toString() {
        return "Job(\""+getIdentifier()+"\", "+getAmount()+" * "
                +getProduct().getName()+", ["+getStartDate()+","+getEndDate()+"])";
    }
}
