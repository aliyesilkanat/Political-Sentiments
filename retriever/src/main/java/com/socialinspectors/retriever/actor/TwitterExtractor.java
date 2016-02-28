package com.socialinspectors.retriever.actor;

import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;
import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.retriever.settings.locations.FetchingLocationsHolder;
import com.socialinspectors.utils.OntologyProperties;
import com.socialinspectors.utils.messages.MessageCreator;
import com.socialinspectors.utils.messages.actors.AnalyzerMessages;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import twitter4j.Status;

public class TwitterExtractor extends UntypedActor {
	private final Logger logger = LogManager.getLogger(TwitterExtractor.class.getName());

	@Override
	public void onReceive(Object message) throws Exception {
		getLogger().debug("extracting status from twitter stream");

		try {
			Object[] params = (Object[]) message;
			Status status = (Status) params[0];
			int index = (int) params[1];
			JsonObject extract = extractTweetData(status, FetchingLocations.getSettings().get(index));
			send2Analyzer(extract);
			getLogger().trace("extracted status sent to analyzer");
		} catch (Exception e) {
			getLogger().error("cannot cast message to Status", e);
			unhandled(message);
		}

		getContext().stop(getSelf());
	}

	void send2Analyzer(JsonObject extract) {
		ActorSelection receiverActor = getContext()
				.actorSelection(RetrieverSystem.deploymentContext.getGuardianPaths().getAnalyzer());
		receiverActor.tell(MessageCreator.create(AnalyzerMessages.ANALYZE, extract), getSelf());
	}

	public JsonObject extractTweetData(Status status, FetchingLocationsHolder settings) {
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(status.getCreatedAt());
		String tweet = status.getText();
		String tweetUri = extractTweetURL(status);
		String authorScreenName = status.getUser().getScreenName();
		String authorName = status.getUser().getName();
		String authorUri = extractAuthorURL(status);
		String authorProfilePictureUri = status.getUser().getProfileImageURL();
		String city = settings.getCity();
		String state = settings.getState();
		double latitude = settings.getLatitude();
		double longitude = settings.getLongitude();
		JsonObject object = createJsonObejct(date, tweet, tweetUri, authorScreenName, authorName, authorUri,
				authorProfilePictureUri, city, state, latitude, longitude);
		return object;

	}

	private String extractAuthorURL(Status status) {
		return "https://twitter.com/" + status.getUser().getScreenName();
	}

	/**
	 * Creates JsonObject using {@link OntologyProperties}.
	 * 
	 * @param date
	 * @param tweet
	 * @param tweetUri
	 * @param authorScreenName
	 * @param authorName
	 * @param authorUri
	 * @param city
	 * @param state
	 * @param state2
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	private JsonObject createJsonObejct(String date, String tweet, String tweetUri, String authorScreenName,
			String authorName, String authorUri, String authorProfilePic, String city, String state, double latitude,
			double longitude) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OntologyProperties.TWEET, tweet);
		jsonObject.addProperty(OntologyProperties.TWEET_URI, tweetUri);
		jsonObject.addProperty(OntologyProperties.AUTHOR_URI, authorUri);
		jsonObject.addProperty(OntologyProperties.AUTHOR_NAME, authorName);
		jsonObject.addProperty(OntologyProperties.AUTHOR_SCREEN_NAME, authorScreenName);
		jsonObject.addProperty(OntologyProperties.AUTHOR_PROFILE_PICTURE, authorProfilePic);
		jsonObject.addProperty(OntologyProperties.DATE, date);
		if (city != null) {
			jsonObject.addProperty(OntologyProperties.CITY, city);
		}
		if (state != null) {
			jsonObject.addProperty(OntologyProperties.STATE, state);
		}
		jsonObject.addProperty(OntologyProperties.LATITUDE, latitude);
		jsonObject.addProperty(OntologyProperties.LONGITUDE, longitude);
		return jsonObject;
	}

	private String extractTweetURL(Status status) {
		return "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId();
	}

	public Logger getLogger() {
		return logger;
	}
}
