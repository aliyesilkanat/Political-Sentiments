package com.socialinspectors.storer.database.pattern.tweets;

import org.bson.Document;

import com.socialinspectors.storer.database.pattern.Pattern;

public abstract class TweetsPattern implements Pattern {

	protected String authorScreenName;
	protected String authorUri;
	protected String authorUserName;
	protected String city;
	protected String country;
	protected String date;
	protected double locationX;
	protected double locationY;
	protected String state = null;
	protected String tweet;
	protected String tweetUri;
	protected String authorPictureUri;
	protected int politicPersonId;

	public TweetsPattern(String authorUserName, String authorScreenName, String authorUri, String authorPictureUri,
			String tweet, String tweetUri, String date, double locationX, double locationY, String country, String city,
			int politicPersonId) {
		this.authorUserName = authorUserName;
		this.authorScreenName = authorScreenName;
		this.authorUri = authorUri;
		this.tweet = tweet;
		this.authorPictureUri = authorPictureUri;
		this.tweetUri = tweetUri;
		this.date = date;
		this.locationX = locationX;
		this.locationY = locationY;
		this.country = country;
		this.city = city;
		this.politicPersonId = politicPersonId;
	}

	protected Document getBaseDocument() {
		Document document = new Document()
				.append(TweetsPatternConstants.AUTHOR,
						new Document().append(TweetsPatternConstants.USERNAME, authorUserName)
								.append(TweetsPatternConstants.URI, authorUri)
								.append(TweetsPatternConstants.SCREENNAME, authorScreenName)
								.append(TweetsPatternConstants.PROFILE_PICTURE, authorPictureUri))
				.append(TweetsPatternConstants.TWEET, tweet).append(TweetsPatternConstants.URI, getTweetUri())
				.append(TweetsPatternConstants.LOCATION,
						new Document().append(TweetsPatternConstants.X, locationX).append(TweetsPatternConstants.Y,
								locationY))
				.append(TweetsPatternConstants.DATE, date).append(TweetsPatternConstants.COUNTRY, country)
				.append(TweetsPatternConstants.CITY, city).append(TweetsPatternConstants.MENTIONED_PERSON_ID, politicPersonId);

		// If tweet is published from stated country, then add state's name to
		// document.
		if (state != null) {
			document.append(TweetsPatternConstants.STATE, state);
		}

		return document;
	}

	public String getTweetUri() {
		return tweetUri;
	}

}
