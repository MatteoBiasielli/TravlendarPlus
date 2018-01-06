package travlendarplus.notification;

/*
 * Parent thread class to be implemented by each specific event-checking thread. Through this class we are
 * able to keep am high level of abstraction on the object Checking Thread.
 */
public class EventCheckingThread extends Thread {
    protected boolean isActive;
    protected TravlendarContextListener creator;
    /*CONTRSUCTOR*/
    public EventCheckingThread (TravlendarContextListener creator){
        this.creator = creator;
        this.isActive = true;
    }
    /*SETTER*/
    public void setActive(boolean bool){
        this.isActive = bool;
    }
}
