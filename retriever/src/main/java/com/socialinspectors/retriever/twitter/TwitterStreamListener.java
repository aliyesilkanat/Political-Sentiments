package com.socialinspectors.retriever.twitter;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.retriever.actor.TwitterExtractor;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.retriever.settings.users.FetchingPeople;

import akka.actor.ActorRef;
import akka.actor.Props;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.UserMentionEntity;

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
		int politicLeaderId = extractMentionedPoliticLeaderId(status);
		if (politicLeaderId != -1) {
			if (getLogger().isTraceEnabled()) {
				getLogger().trace("found politic leader mention. ID: {}", politicLeaderId);
			}
			ActorRef extractor = RetrieverSystem.system.actorOf(Props.create(TwitterExtractor.class));
			Object[] params = createMessageParams(status, politicLeaderId);
			extractor.tell(params, ActorRef.noSender());
		}
	}

	/**
	 * Extracts mentioned politic leader database id using pre-loaded people
	 * names'.
	 * 
	 * @param status
	 * @return
	 */
	private int extractMentionedPoliticLeaderId(Status status) {
		int politicPersonId = -1;
		Iterator<String> iterator = FetchingPeople.getSettings().keySet().iterator();

		while (iterator.hasNext()) {
			String personName = iterator.next();
			for (UserMentionEntity mention : status.getUserMentionEntities()) {
				if (mention.getScreenName().equals(personName)) {
					politicPersonId = FetchingPeople.getSettings().get(personName).getId();
					break;
				}

			}
		}
		return politicPersonId;

	}

	private Object[] createMessageParams(Status status, int politicLeaderId) {
		Object[] params = new Object[3];
		params[0] = status;
		params[1] = locationIndex;
		params[2] = politicLeaderId;
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
