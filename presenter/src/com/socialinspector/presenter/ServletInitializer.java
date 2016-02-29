package com.socialinspector.presenter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.socialinspectors.utils.deployment.DeploymentContext;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.prop.DeploymentConstants;

/**
 * Application Lifecycle Listener implementation class ServletInitializer
 *
 */
@WebListener
public class ServletInitializer implements ServletContextListener {

	private static final String[] LOCALE = new String[] { DeploymentConstants.LOCALE };
	private static final String[] REMOTE = new String[] { DeploymentConstants.REMOTE };
	public static DeploymentContext context;

	/**
	 * Default constructor.
	 */
	public ServletInitializer() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// context = new DeploymentContext(REMOTE, SystemNames.TRIGGER);
	}

}
