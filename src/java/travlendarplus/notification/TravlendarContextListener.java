package travlendarplus.notification;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Modified by mattiadifatta on 24/11/2017.
 */
@WebListener
public class TravlendarContextListener implements ServletContextListener{
    private EventCheckingThread checker = null;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        if(this.checker == null || !this.checker.isAlive()) {
            checker = createThread(this);
            checker.start();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        checker.setActive(false);
    }

    public EventCheckingThread createThread(TravlendarContextListener creator){
        return new EventCheckingThread(creator);
    }

    public void setChecker(EventCheckingThread thread){
        this.checker = thread;
    }
}
