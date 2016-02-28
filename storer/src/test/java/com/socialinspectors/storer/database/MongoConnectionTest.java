package com.socialinspectors.storer.database;

import java.util.Date;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Test;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.socialinspectors.data.DatabaseConstants;

public class MongoConnectionTest {
	@Test
	public void testConnection() throws Exception {
		try {
			MongoDatabase database = MongoConnector.getDatabase();
			database.getCollection(DatabaseConstants.TEST_TWEETS_COLLECTION);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void oyıun() throws Exception {
		MongoCollection<Document> collection = MongoConnector.getDatabase()
				.getCollection(DatabaseConstants.TEST_TWEETS_COLLECTION);
		collection.insertOne(new Document().append("date", new Date().toString()));

	}
}
