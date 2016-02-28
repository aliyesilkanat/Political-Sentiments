package com.socialinspectors.storer.database.pattern.tweets;

import org.bson.Document;

public class NonTaggedTweetsPattern extends TweetsPattern {

	/**
	 * Constructor for Tweet Pattern. DOES NOT INCLUDES STATE.
	 * 
	 * @param authorUserName
	 * @param authorScreenName
	 * @param authorUri
	 * @param tweet
	 * @param tweetUri
	 * @param date
	 * @param locationX
	 * @param locationY
	 * @param country
	 * @param city
	 */
	public NonTaggedTweetsPattern(String authorUserName, String authorScreenName, String authorUri,
			String authorPictureUri, String tweet, String tweetUri, String date, double locationX, double locationY,
			String country, String city, int politicPersonId) {
		super(authorUserName, authorScreenName, authorUri, authorPictureUri, tweet, tweetUri, date, locationX,
				locationY, country, city, politicPersonId);
	}

	/**
	 * Constructor for Tweet Pattern class. Includes state, country, tweet, and
	 * author information.
	 * 
	 * @param authorUserName
	 * @param authorUri
	 * @param tweet
	 * @param tweetUri
	 * @param date
	 * @param locationX
	 * @param locationY
	 * @param country
	 * @param state
	 * @param city
	 */
	public NonTaggedTweetsPattern(String authorUserName, String authorScreenName, String authorUri,
			String authorPictureUri, String tweet, String tweetUri, String date, double locationX, double locationY,
			String country, String state, String city, int politicPersonId) {
		super(authorUserName, authorScreenName, authorUri, authorPictureUri, tweet, tweetUri, date, locationX,
				locationY, country, city, politicPersonId);
		this.state = state;
	}

	public Document getDocument() {

		return getBaseDocument();
	}

}
