package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dao.MongoClientSingleton;

public class WherebyServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("started");
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("stopped");
		MongoClientSingleton.closeMongoClientInstance();
	}

}
