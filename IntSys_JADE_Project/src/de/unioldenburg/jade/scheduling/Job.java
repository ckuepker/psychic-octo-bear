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
    private int priority;

    /**
     * Constructor for ids as Integer and priority (needed for JSON-file)
     * @author Armin Pistoor
     */
    public Job(int identifier, Product product, int amount, int startDate, int endDate, int priority) {
        this.identifier = identifier + "";
        this.product = product;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
    }
    
    public Job(String identifier, Product product, int amount, int startDate, int endDate) {
        this.identifier = identifier;
        this.product = product;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getIdentifier() {
        return this.identifier;
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
    
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public String toString() {
        return "Job(\""+getIdentifier()+"\", "+getAmount()+" * "
                +getProduct().getName()+", ["+getStartDate()+","+getEndDate()+"])";
    }
}
