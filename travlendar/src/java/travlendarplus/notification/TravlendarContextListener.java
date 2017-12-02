package travlendarplus.notification;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * This class implements a Context Listener for the application server used (GlassFish 4.1.1).
 */
@WebListener
public class TravlendarContextListener implements ServletContextListener{
    /**
     * Thread to start at server deployment.
     */
    private WeatherCheckingThread checkerW = null;
    /**
     * Thread to start at server deployment
     */
    private ActivitiesCheckingThread checkerAct = null;

    /**
     * Overridden method to execute code at server deployment/start
     * @param servletContextEvent
     */
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

    /**
     * Overridden method to execute code at server stop
     * @param servletContextEvent
     */
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        checkerW.setActive(false);
        checkerAct.setActive(false);
    }

    /*THREADS CREATORS*/
    public WeatherCheckingThread createWeatherThread(TravlendarContextListener creator){
        return new WeatherCheckingThread(creator);
    }

    public ActivitiesCheckingThread createActivitiesThread(TravlendarContextListener creator){
        return new ActivitiesCheckingThread(creator);
    }

    /*SETTERS*/
    public void setCheckerW(WeatherCheckingThread thread){
        this.checkerW = thread;
    }

    public void setCheckerAct(ActivitiesCheckingThread thread){
        this.checkerAct = thread;
    }
}
