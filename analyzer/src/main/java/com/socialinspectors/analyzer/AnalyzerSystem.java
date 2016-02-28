package com.socialinspectors.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.ActorSystem;
import akka.actor.Props;

import com.socialinspectors.analyzer.actor.GuardianAnalyzer;
import com.socialinspectors.deployment.DeploymentContext;
import com.socialinspectors.deployment.constants.GuardianActorNames;
import com.socialinspectors.deployment.constants.SystemNames;
import com.typesafe.config.ConfigFactory;

public class AnalyzerSystem {
	public static DeploymentContext deploymentContext;
	/**
	 * Logger instance
	 */
	private static final Logger logger = LogManager
			.getLogger(AnalyzerSystem.class.getName());

	/**
	 * {@link ActorSystem} instance for retriever system.
	 */
	public static ActorSystem system;

	public static Logger getLogger() {
		return logger;
	}

	/**
	 * Starting main method for system.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		deploymentContext = new DeploymentContext(args, SystemNames.ANALYZER);
		try {
			new AnalyzerSystem().execute();
		} catch (Exception e) {
			getLogger().fatal(
					"system initalization error, shutting down system", e);
		}

	}

	/**
	 * Creates leader retriever actor.
	 */
	private void createLeaderActor() {
		system.actorOf(Props.create(GuardianAnalyzer.class),
				GuardianActorNames.ANALYZER_GUARDIAN);
	}

	private void execute() {
		startSystem();
		createLeaderActor();
	}

	private void startSystem() {
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("Starting analyzer system");
		}
		system = ActorSystem.create(SystemNames.ANALYZER, ConfigFactory
				.load(ConfigFactory.parseString(deploymentContext
						.getAkkaConfig())));
		if (getLogger().isInfoEnabled()) {
			getLogger().info("Started analyzer system");
		}
	}
}
