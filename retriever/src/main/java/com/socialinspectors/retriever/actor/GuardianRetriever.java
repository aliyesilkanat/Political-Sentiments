package com.socialinspectors.retriever.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.retriever.actor.dispatcher.GuardianRetrieverDispatcher;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.utils.actor.Actor;

import akka.actor.ActorRef;
import akka.actor.Props;

/**
 * @author Ali Yesilkanat.
 * 
 *         Class for Leader Retriever Actor
 *
 */
public class GuardianRetriever extends Actor {
	public GuardianRetriever() {
		super(new GuardianRetrieverDispatcher());

	}

	private final Logger logger = LogManager.getLogger(GuardianRetriever.class.getName());

	public Logger getLogger() {
		return logger;
	}

	public void onStart() {
		getLogger().info("starting fetching session");
		for (int i = 0; i < FetchingLocations.getSettings().size(); i++) {
			createFetchingActors(i);
		}
		// getLogger().info("creating twitter stream");
		// setTwitterStream(new
		// TwitterStreamBuilder().build(FetchingSettings.getSettings().getLatitude(),
		// FetchingSettings.getSettings().getLongitude(), new
		// TwitterStreamListener()));
		// if (getLogger().isDebugEnabled()) {
		// getLogger().debug("created twitter stream");
		// }
	}

	/**
	 * Creates fetching actors for each location set in
	 * {@link FetchingLocations}.
	 * 
	 * @param i
	 *            Location index of {@link FetchingLocations}.
	 */
	private void createFetchingActors(int i) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("creating fetching actors, index: {}", i);
		}
		ActorRef fetcherActor = getContext().actorOf(Props.create(TwitterFetcher.class));
		fetcherActor.tell(i, ActorRef.noSender());

	}

}
