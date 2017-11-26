package travlendarplus.notification;

import travlendarplus.data.DataLayer;

import java.sql.SQLException;

/**
 * Modified by mattiadifatta on 24/11/2017.
 */
public class EventCheckingThread extends Thread {
    private final long FIVE_MINUTES = 5*60*1000;
    private boolean isActive;
    private TravlendarContextListener creator;

    public EventCheckingThread (TravlendarContextListener creator){
        this.creator = creator;
    }

    @Override
    public void run() {
        try {
            while (this.isActive) {
                //DELETE NOTIFICATIONS OLDER THAN 24 HRS
                try {
                    DataLayer.deleteOldNotification();
                }catch (SQLException e){
                    System.out.println("Unable to delete oldest notifications."); //TODO: come la gestiamo?
                }
                //CREATE NOTIFICATIONS FROM APIS

                //CREATE NOTIFICATIONS FOR USERS

                sleep(FIVE_MINUTES);

            }
        }catch (InterruptedException e){
            EventCheckingThread temp = creator.createThread(creator);
            creator.setChecker(temp);
            temp.start();
        }
    }

    public void setActive(boolean bool){
        this.isActive = bool;
    }
}
