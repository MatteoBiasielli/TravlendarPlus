package travlendarplus.notification;

import travlendarplus.data.DataLayer;

import java.sql.SQLException;
import java.util.logging.*;

/**
 * Modified by mattiadifatta on 28/11/2017.
 */
public class ActivitiesCheckingThread extends EventCheckingThread {

    private final static Logger LOGGER = Logger.getLogger ( Class.class.getName());

    private final long FIVE_MINUTES = 5*60*1000;

    public ActivitiesCheckingThread(TravlendarContextListener creator){
        super(creator);
    }

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
