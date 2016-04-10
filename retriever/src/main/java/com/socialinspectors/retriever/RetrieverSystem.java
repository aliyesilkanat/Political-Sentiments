package com.socialinspectors.retriever;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.utils.deployment.constants.GuardianActorNames;
import com.socialinspectors.retriever.actor.GuardianRetriever;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.retriever.settings.users.FetchingPeople;
import com.socialinspectors.utils.deployment.DeploymentContext;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.deployment.paths.GuardianPaths;
import com.socialinspectors.utils.messages.MessageCreator;
import com.socialinspectors.utils.messages.actors.RetrieverMessages;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import twitter4j.TwitterStream;

/**
 * @author Ali Yesilkanat.
 * 
 *         Class for starting retriever Akka system.
 */
public class RetrieverSystem {
	/**
	 * Logger instance
	 */
	private static final Logger logger = LogManager.getLogger(RetrieverSystem.class.getName());
	public static DeploymentContext deploymentContext;

	public static Logger getLogger() {
		return logger;
	}

	private RetrieverSystem() {
	}

	/**
	 * Starting main method for system.
	 * 
	 * @param deploymentType
	 */
	public static void main(String[] args) {

		deploymentContext = new DeploymentContext(args, SystemNames.RETRIEVER);
		try {
			new RetrieverSystem().execute();
		} catch (Exception e) {
			getLogger().fatal("system initalization error, shutting down system", e);
		}
	}

	/**
	 * {@link ActorSystem} instance for retriever system.
	 */
	public static ActorSystem system;

	/**
	 * Creates leader retriever actor.
	 */
	private void createGuardianActor() {
		system.actorOf(Props.create(GuardianRetriever.class), GuardianActorNames.RETRIEVER_GUARDIAN);
	}

	private void execute() {
		startActorSystem();
		createGuardianActor();
		loadFetchingSettings();
		startGuardianActor();

	}

	private void startGuardianActor() {
		system.actorSelection(deploymentContext.getGuardianPaths().getRetriever())
				.tell(MessageCreator.create(RetrieverMessages.START, null), ActorRef.noSender());
	}

	private void loadFetchingSettings() {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("loading fetching settings");
		}

		FetchingPeople.getSettings();
		FetchingLocations.getSettings();
		getLogger().info("loaded fetching settings successfully");

	}

	private void startActorSystem() {
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("Starting retriever system");
		}

		system = ActorSystem.create(SystemNames.RETRIEVER,
				ConfigFactory.parseString(deploymentContext.getAkkaConfig()));
		if (getLogger().isInfoEnabled()) {
			getLogger().info("Started retriever system");
		}
	}
}
