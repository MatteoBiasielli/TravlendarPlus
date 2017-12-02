package travlendarplus.notification;

import travlendarplus.data.DataLayer;

import java.sql.SQLException;
import java.util.logging.*;

/**
 * This class implements the generic EventCheckingThread class for users' activities.
 */
public class ActivitiesCheckingThread extends EventCheckingThread {
    /**
     * Logger of this class used to handle internal exceptions
     */
    private final static Logger LOGGER = Logger.getLogger ( Class.class.getName());
    /**
     *  Constant representing the interval of time (in millis) this thread must sleep between each check.
     */
    private final long FIVE_MINUTES = 5*60*1000;
    /*CONSTRUCTOR*/
    public ActivitiesCheckingThread(TravlendarContextListener creator){
        super(creator);
    }

    /**
     * Override of the inherited method run() in which in this thread first deletes notifications older than 24 hrs,
     * then searches for imminent activities and generates notifications, finally it goes sleeping for FIVE_MINUTES
     */
    @Override
    public void run() {
        try {
            while(this.isActive) {
                try {
                    //DELETE OLD NOTIFICATION
                    DataLayer.deleteOldNotification();
                    //CREATE NOTIFICATIONS FOR USERS
                    DataLayer.checkForNotification();
                } catch (SQLException e) {
                    LOGGER.log(Level.INFO, e.getMessage());
                }
                sleep(FIVE_MINUTES);
            }
        } catch (InterruptedException e) {
            ActivitiesCheckingThread temp = creator.createActivitiesThread(creator);
            creator.setCheckerAct(temp);
            temp.start();
        }
    }
}
