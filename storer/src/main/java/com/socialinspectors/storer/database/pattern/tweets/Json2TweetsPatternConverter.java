package com.socialinspectors.storer.database.pattern.tweets;

import com.google.gson.JsonObject;
import com.socialinspectors.utils.OntologyProperties;

public class Json2TweetsPatternConverter {

	public TweetsPattern convert(JsonObject tweetObject) {

		String authorName = tweetObject.get(OntologyProperties.AUTHOR_NAME).getAsString();
		String authorUri = tweetObject.get(OntologyProperties.AUTHOR_URI).getAsString();
		String authorScreenName = tweetObject.get(OntologyProperties.AUTHOR_SCREEN_NAME).getAsString();
		String city = tweetObject.get(OntologyProperties.CITY).getAsString();
		String date = tweetObject.get(OntologyProperties.DATE).getAsString();
		double latitude = tweetObject.get(OntologyProperties.LATITUDE).getAsDouble();
		double longitude = tweetObject.get(OntologyProperties.LONGITUDE).getAsDouble();
		String tweet = tweetObject.get(OntologyProperties.TWEET).getAsString();
		String tweetUri = tweetObject.get(OntologyProperties.TWEET_URI).getAsString();
		String authorPictureUri = tweetObject.get(OntologyProperties.AUTHOR_PROFILE_PICTURE).getAsString();
		int politicPersonId = tweetObject.get(OntologyProperties.PERSON_ID).getAsInt();
		return fillPattern(tweetObject, authorName, authorUri, authorScreenName, authorPictureUri, city, date, latitude,
				longitude, tweet, tweetUri, politicPersonId);
	}

	private TweetsPattern fillPattern(JsonObject tweetObject, String authorName, String authorUri,
			String authorScreenName, String authorPictureUri, String city, String date, double latitude,
			double longitude, String tweet, String tweetUri, int politicPersonId) {
		TweetsPattern pattern;
		if (tweetObject.has(OntologyProperties.TAG)) {
			JsonObject tagObject = tweetObject.get(OntologyProperties.TAG).getAsJsonObject();
			int tagId = tagObject.get(OntologyProperties.TAG_ID).getAsInt();
			int tagResult = tagObject.get(OntologyProperties.TAG_RESULT).getAsInt();
			pattern = getSuitableTaggedPattern(tweetObject, authorName, authorUri, authorScreenName, authorPictureUri,
					city, date, latitude, longitude, tweet, tweetUri, tagId, tagResult, politicPersonId);
		} else {
			pattern = getSuitableNonTaggedPattern(tweetObject, authorName, authorUri, authorScreenName,
					authorPictureUri, city, date, latitude, longitude, tweet, tweetUri,politicPersonId);
		}
		return pattern;
	}

	private TweetsPattern getSuitableTaggedPattern(JsonObject tweetObject, String authorName, String authorUri,
			String authorScreenName, String authorPictureUri, String city, String date, double latitude,
			double longitude, String tweet, String tweetUri, int tagId, int tagResult, int politicPersonId) {
		TaggedTweetsPattern pattern;
		if (tweetObject.has(OntologyProperties.STATE)) {
			String state = tweetObject.get(OntologyProperties.STATE).getAsString();
			pattern = new TaggedTweetsPattern(authorName, authorScreenName, authorUri, authorPictureUri, tweet,
					tweetUri, date, latitude, longitude, "U.S.A.", city, state, tagId, tagResult, politicPersonId);
		} else {
			pattern = new TaggedTweetsPattern(authorName, authorScreenName, authorUri, authorPictureUri, tweet,
					tweetUri, date, latitude, longitude, "U.S.A.", city, tagId, tagResult, politicPersonId);
		}
		return pattern;
	}

	private TweetsPattern getSuitableNonTaggedPattern(JsonObject tweetObject, String authorName, String authorUri,
			String authorScreenName, String authorPictureUri, String city, String date, double latitude,
			double longitude, String tweet, String tweetUri, int politicPersonId) {
		NonTaggedTweetsPattern pattern;
		if (tweetObject.has(OntologyProperties.STATE)) {
			String state = tweetObject.get(OntologyProperties.STATE).getAsString();
			pattern = new NonTaggedTweetsPattern(authorName, authorScreenName, authorUri, authorPictureUri, tweet,
					tweetUri, date, latitude, longitude, "U.S.A.", city, state, politicPersonId);
		} else {
			pattern = new NonTaggedTweetsPattern(authorName, authorScreenName, authorUri, authorPictureUri, tweet,
					tweetUri, date, latitude, longitude, "U.S.A.", city, politicPersonId);
		}
		return pattern;
	}
}
