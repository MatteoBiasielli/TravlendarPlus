package travlendarplus.notification;

/*
 * Modified by mattiadifatta on 24/11/2017.
 */
public class EventCheckingThread extends Thread {
    protected boolean isActive;
    protected TravlendarContextListener creator;

    public EventCheckingThread (TravlendarContextListener creator){
        this.creator = creator;
        this.isActive = true;
    }

    public void setActive(boolean bool){
        this.isActive = bool;
    }
}
