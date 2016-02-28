package com.socialinspectors.storer.database.pattern.tweets;

import org.bson.Document;

public class TaggedTweetsPattern extends TweetsPattern {

	private int tagId;
	private int tagResult;

	public TaggedTweetsPattern(String authorUserName, String authorScreenName,
			String authorUri, String authorPictureUri,String tweet, String tweetUri, String date,
			double locationX, double locationY, String country, String city,
			int tagId, int tagResult) {
		super(authorUserName, authorScreenName, authorUri, authorPictureUri,tweet, tweetUri,
				date, locationX, locationY, country, city);
		this.tagId = tagId;
		this.tagResult = tagResult;
	}

	public TaggedTweetsPattern(String authorUserName, String authorScreenName,
			String authorUri, String authorPictureUri,String tweet, String tweetUri, String date,
			double locationX, double locationY, String country, String city,
			String state, int tagId, int tagResult) {
		super(authorUserName, authorScreenName, authorUri,authorPictureUri, tweet, tweetUri,
				date, locationX, locationY, country, city);
		this.tagId = tagId;
		this.tagResult = tagResult;
		this.state = state;
	}

	@Override
	public Document getDocument() {
		return getBaseDocument().append(
				TweetsPatternConstants.TAG,
				new Document().append(TweetsPatternConstants.TAG_ID, tagId)
						.append(TweetsPatternConstants.TAG_RESULT, tagResult));
	}
}
