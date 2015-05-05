package de.unioldenburg.jade.contractnet.data;

import de.unioldenburg.jade.contractnet.agents.Participant;
import jade.lang.acl.ACLMessage;

/**
 * Data structure for Jobs created by an auctioneer.
 * @author Christoph Küpker
 */
public class Job {
    
    private String id;
    private int starttime, endtime;
    
    public Job(String id, int starttime, int endtime) {
        this.id = id;
        this.starttime = starttime;
        this.endtime = endtime;
    }
        
    /**
     * Tries to parse a Job from a given message by accessing user set 
     * parameters with keys defined in Participant
     * @param msg
     * @return 
     */
    public static Job parseJobFromMessage(ACLMessage msg) {
        String id = msg.getUserDefinedParameter(Participant.KEY_ID);
        int starttime = Integer.parseInt(msg.getUserDefinedParameter(Participant.KEY_START));
        int endtime = Integer.parseInt(msg.getUserDefinedParameter(Participant.KEY_END));
        return new Job(id, starttime, endtime);
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the starttime
     */
    public int getStarttime() {
        return starttime;
    }

    /**
     * @param starttime the starttime to set
     */
    public void setStarttime(int starttime) {
        this.starttime = starttime;
    }

    /**
     * @return the endtime
     */
    public int getEndtime() {
        return endtime;
    }

    /**
     * @param endtime the endtime to set
     */
    public void setEndtime(int endtime) {
        this.endtime = endtime;
    }
    
    @Override
    public String toString() {
        return "Job("+getId()+","+getStarttime()+","+getEndtime()+")";
    }
}
