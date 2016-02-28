package com.socialinspectors.storer.database;

import org.bson.Document;
import org.junit.Test;
import org.mockito.Mockito;

import com.socialinspectors.data.DatabaseConstants;
import com.socialinspectors.storer.database.pattern.tag.TagsPattern;
import com.socialinspectors.storer.database.pattern.tweets.NonTaggedTweetsPattern;

public class InsertQueriesTests {

	@Test
	public void testTweetInsertion() throws Exception {
		// setup
		Document document = new NonTaggedTweetsPattern("aliyesilkanat", "Ali Yeşilkanat",
				"https://twitter.com/aliyesilkanat",
				"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg",
				"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave … on #SoundCloud",
				"https://twitter.com/aliyesilkanat/status/637643909645094912", "2015-08-29", 38.476909, 27.064476,
				"Turkey", "IZMIRSTATE", "Izmir",1).getDocument();
		InsertQueries queries = Mockito.spy(new InsertQueries());
		Mockito.doReturn(MongoConnector.getDatabase().getCollection(DatabaseConstants.TEST_TWEETS_COLLECTION)).when(queries)
				.getCollection(DatabaseConstants.TWEETS_COLLECTION);

		// execute
		queries.insertTweets(document);
	}

	@Test
	public void testTagInsertion() throws Exception {
		// setup
		Document document = new TagsPattern(1, "OpenNLPClassifier", "Apache OpenNLP used classifier algorithm")
				.getDocument();
		InsertQueries queries = Mockito.spy(new InsertQueries());
		Mockito.doReturn(MongoConnector.getDatabase().getCollection(DatabaseConstants.TECHNIQUES_COLLECTION)).when(queries)
				.getCollection(DatabaseConstants.TECHNIQUES_COLLECTION);

		// execute
		queries.insertTags(document);
	}
}
