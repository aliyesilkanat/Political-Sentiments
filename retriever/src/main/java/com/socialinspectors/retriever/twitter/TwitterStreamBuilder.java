package com.socialinspectors.retriever.twitter;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.retriever.settings.users.FetchingPeople;

import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamBuilder {
	private static final String consumerKey = "tOYxtNm9sk6Bzgs97BBmWMnPZ";
	private static final String consumerSecret = "DTABuXfPAYwsiJ0tZWwlejBL1oCmqfTeL9t2bI4sWI3MGeQcs8";
	private static final String accessToken = "18263321-aIQ5RWXyKdhMlA6mYs5wjSUn1Yq3LOXjVle0jhhvC";
	private static final String accessTokenSecret = "mEKfWKSQ4C4FjTf4uileWJ0c4QLSJw4HNMOUngWlLhmLV";

	private static final Logger logger = LogManager.getLogger(TwitterStreamBuilder.class);

	/**
	 * Creates configuration using application credentials for Twitter Stream
	 * Factory.
	 * 
	 * @return configuration file.
	 */
	private Configuration createConfig() {
		ConfigurationBuilder configBuilder = new ConfigurationBuilder();
		configBuilder.setDebugEnabled(true).setOAuthConsumerKey(consumerKey).setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken).setOAuthAccessTokenSecret(accessTokenSecret);
		Configuration configuration = configBuilder.build();
		return configuration;
	}

	/**
	 * Builds a Twitter Stream from given latitude and longitude coordinate
	 * elements and adds given {@link StatusListener} listener.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param listener
	 * @return Built Twitter Stream.
	 */
	public TwitterStream build(double latitude, double longitude, StatusListener listener) {
		TwitterStreamFactory twitterStreamFactory = new TwitterStreamFactory(createConfig());
		TwitterStream twitterStream = twitterStreamFactory.getInstance();
		twitterStream.addListener(listener);
		FilterQuery filterQuery = new FilterQuery();
		filterQuery.locations(createBoundingBox(latitude, longitude));
		filterQuery.language(new String[] { "en" });

		Set<String> keySet = FetchingPeople.getSettings().keySet();
		String[] filterMentions = new String[keySet.size()];
		filterMentions = (String[]) keySet.toArray(filterMentions);
		for (int i = 0; i < filterMentions.length; i++) {
			filterMentions[i] = "@".concat(filterMentions[i]);
		}
		filterQuery.track(filterMentions);

		twitterStream.filter(filterQuery);
		return twitterStream;
	}

	/**
	 * Creates a bounding box using given coordinate elements.
	 * 
	 * This method swaps latitude and longitude variables. Because Twitter
	 * Filter Query use it as that way
	 * 
	 * @param latitude
	 * @param longitude
	 * @return double 2d array of two locations which are boundaries of a box.
	 */
	private double[][] createBoundingBox(double latitude, double longitude) {
		double var = longitude;
		longitude = latitude;
		latitude = var;
		double rate = 0.5;
		double[][] box = { { latitude - rate, longitude - rate }, { latitude + rate, longitude + rate }

		};
		return box;
	}

	public static Logger getLogger() {
		return logger;
	}
}
