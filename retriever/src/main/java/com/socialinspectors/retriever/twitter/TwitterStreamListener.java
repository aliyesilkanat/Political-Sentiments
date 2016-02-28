package com.socialinspectors.retriever.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import akka.actor.ActorRef;
import akka.actor.Props;

import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.retriever.actor.TwitterExtractor;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;

public class TwitterStreamListener implements StatusListener {
	private static final Logger logger = LogManager.getLogger(TwitterStreamListener.class.getName());
	private int locationIndex;

	/**
	 * Listens twitter responses from given location's index.
	 * 
	 * @param locationIndex
	 *            fetching location index on {@link FetchingLocations}
	 */
	public TwitterStreamListener(int locationIndex) {
		this.setLocationIndex(locationIndex);
	}

	@Override
	public void onException(Exception ex) {
		getLogger().error("error while fetching twitter stream ", ex);
	}

	@Override
	public void onStatus(Status status) {
		getLogger().debug("fetching from twitter stream, status: {}", status.getText());
		ActorRef extractor = RetrieverSystem.system.actorOf(Props.create(TwitterExtractor.class));
		Object[] params = createMessageParams(status);
		extractor.tell(params, ActorRef.noSender());
	}

	private Object[] createMessageParams(Status status) {
		Object[] params = new Object[2];
		params[0] = status;
		params[1] = locationIndex;
		return params;
	}

	@Override
	public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
	}

	@Override
	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
	}

	@Override
	public void onScrubGeo(long userId, long upToStatusId) {
	}

	@Override
	public void onStallWarning(StallWarning warning) {
	}

	public static Logger getLogger() {
		return logger;
	}

	public int getLocationIndex() {
		return locationIndex;
	}

	public void setLocationIndex(int locationIndex) {
		this.locationIndex = locationIndex;
	}

}
