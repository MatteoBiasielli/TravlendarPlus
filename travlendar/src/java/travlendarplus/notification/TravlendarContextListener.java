package travlendarplus.notification;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Modified by mattiadifatta on 24/11/2017.
 */
@WebListener
public class TravlendarContextListener implements ServletContextListener{
    private WeatherCheckingThread checkerW = null;
    private ActivitiesCheckingThread checkerAct = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if(this.checkerW == null || !this.checkerW.isAlive()) {
            checkerW = createWeatherThread(this);
            checkerW.start();
        }

        if(this.checkerAct == null || !this.checkerAct.isAlive()) {
            checkerAct = createActivitiesThread(this);
            checkerAct.start();
        }


    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        checkerW.setActive(false);
        checkerAct.setActive(false);
    }

    public WeatherCheckingThread createWeatherThread(TravlendarContextListener creator){
        return new WeatherCheckingThread(creator);
    }

    public ActivitiesCheckingThread createActivitiesThread(TravlendarContextListener creator){
        return new ActivitiesCheckingThread(creator);
    }

    public void setCheckerW(WeatherCheckingThread thread){
        this.checkerW = thread;
    }

    public void setCheckerAct(ActivitiesCheckingThread thread){
        this.checkerAct = thread;
    }
}
