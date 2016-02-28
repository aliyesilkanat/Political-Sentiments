package com.socialinspectors.retriever.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.retriever.settings.locations.FetchingLocationsHolder;
import com.socialinspectors.retriever.twitter.TwitterStreamBuilder;
import com.socialinspectors.retriever.twitter.TwitterStreamListener;

import akka.actor.UntypedActor;

public class TwitterFetcher extends UntypedActor {
	private static final Logger logger = LogManager.getLogger(TwitterFetcher.class);

	@Override
	public void onReceive(Object message) throws Exception {
		int locationIndex = (int) message;
		createTwitterStream(locationIndex);
	}

	private void createTwitterStream(int locationIndex) {
		getLogger().info("creating twitter stream");
		FetchingLocationsHolder fetchingLocationsHolder = FetchingLocations.getSettings().get(locationIndex);
		new TwitterStreamBuilder().build(fetchingLocationsHolder.getLatitude(), fetchingLocationsHolder.getLongitude(),
				new TwitterStreamListener(locationIndex));
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("created twitter stream");
		}
	}

	public static Logger getLogger() {
		return logger;
	}

}
