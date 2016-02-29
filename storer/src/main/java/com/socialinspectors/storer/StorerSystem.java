package com.socialinspectors.storer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.ActorSystem;
import akka.actor.Props;

import com.socialinspectors.utils.deployment.constants.GuardianActorNames;
import com.socialinspectors.storer.actor.GuardianStorer;
import com.socialinspectors.utils.deployment.DeploymentContext;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.typesafe.config.ConfigFactory;

/**
 * @author Ali Yeşilkanat.
 * 
 *         Class for starting storer akka system.
 */
public class StorerSystem {
	/**
	 * Logger instance
	 */
	private static final Logger logger = LogManager.getLogger(StorerSystem.class.getName());
	public static DeploymentContext deploymentContext;

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Starting main method for system.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		deploymentContext = new DeploymentContext(args, SystemNames.STORER);
		new StorerSystem().execute();

	}

	/**
	 * {@link ActorSystem} instance for retriever system.
	 */
	public static ActorSystem system;

	/**
	 * Creates leader retriever actor.
	 */
	private void createLeaderActor() {
		system.actorOf(Props.create(GuardianStorer.class), GuardianActorNames.STORER_GUARDIAN);
	}

	private void execute() {
		startSystem();
		createLeaderActor();
	}

	private void startSystem() {
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("Starting storer system");
		}
		system = ActorSystem.create(SystemNames.STORER, ConfigFactory.parseString(deploymentContext.getAkkaConfig()));
		if (getLogger().isInfoEnabled()) {
			getLogger().info("Started storer system");
		}
	}
}
