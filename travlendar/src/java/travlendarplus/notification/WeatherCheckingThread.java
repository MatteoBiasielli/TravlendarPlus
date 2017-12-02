package travlendarplus.notification;

import travlendarplus.apimanager.APIManager;
import travlendarplus.data.DataLayer;
import travlendarplus.exceptions.InvalidInputException;
import travlendarplus.notification.weather.Forecast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements the generic EventCheckingThread class for weather forecast from the Yahoo!Weather APIs
 */
public class WeatherCheckingThread extends EventCheckingThread{
    /**
     * Logger for this class used to handle internal exceptions.
     */
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    /**
     * Constant representing the position of today in an array of data.
     */
    private final int TODAY = 0;

    /**
     * Constant representing the interval of time (in millis) this thread must sleep between each check.
     */
    private final long A_DAY = 24 * 60 * 60 * 1000;
    /*CONSTRUCTOR*/
    public WeatherCheckingThread(TravlendarContextListener creator){
        super(creator);
    }

    /**
     * Override of the run() method in which this thread retrieves weather forecasts from Yahoo!Weather APIs,
     * generates a new notifications and stores it in the DB. Finally it sleeps for A_DAY
     */
    @Override
    public void run() {
        try {
            while (this.isActive){
                try {
                    //CREATE NOTIFICATIONS FROM APIS
                    ArrayList<Forecast> forecasts = APIManager.getYahooWeatherForcast("Milan");
                    Notification weather = new Notification(forecasts.get(TODAY).getWeatherText(), new Date().getTime());
                    DataLayer.addNotificationToDB(weather);
                }catch (SQLException e){
                    LOGGER.log(Level.INFO, e.getMessage());
                }catch (InvalidInputException e){
                    LOGGER.log(Level.INFO, "Not a valid city!");
                }
                sleep(A_DAY);
            }
        } catch (InterruptedException e) {
            WeatherCheckingThread temp = creator.createWeatherThread(creator);
            creator.setCheckerW(temp);
            temp.start();
        }
    }
}
