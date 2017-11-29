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
 * Modified by mattiadifatta on 28/11/2017.
 */
public class WeatherCheckingThread extends EventCheckingThread{

    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

    private final int TODAY = 0;
    private final long A_DAY = 24 * 60 * 60 * 1000;

    public WeatherCheckingThread(TravlendarContextListener creator){
        super(creator);
    }

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
