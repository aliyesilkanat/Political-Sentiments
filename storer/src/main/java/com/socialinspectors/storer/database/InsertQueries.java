package com.socialinspectors.storer.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.socialinspectors.data.DatabaseConstants;

public class InsertQueries {
	private static final Logger logger = LogManager.getLogger(InsertQueries.class);

	/**
	 * Inserts {@link Document } object into "tweets" collection.
	 * 
	 * @param inserted
	 *            document.
	 */
	public void insertTweets(Document insertedDocument) {
		String tweet = insertedDocument.getString("tweet");
		getLogger().debug("inserting tweets: {}", tweet);

		MongoCollection<Document> collection = getCollection(DatabaseConstants.TWEETS_COLLECTION);
		collection.insertOne(insertedDocument);

		getLogger().trace("inserted tweets: {}", tweet);

	}

	/**
	 * Checks if collection name is in db.
	 * 
	 * @param collectionName
	 *            name of collection
	 * @return {@link MongoCollection} collection.
	 */
	MongoCollection<Document> getCollection(String collectionName) {
		MongoCollection<Document> collection = null;
		if (collectionName.equals(DatabaseConstants.TWEETS_COLLECTION)) {
			collection = MongoConnector.getDatabase().getCollection(DatabaseConstants.TWEETS_COLLECTION);
		} else if (collectionName.equals(DatabaseConstants.TECHNIQUES_COLLECTION)) {
			collection = MongoConnector.getDatabase().getCollection(DatabaseConstants.TECHNIQUES_COLLECTION);
		} else {
			getLogger().fatal("not found collection: {}", collectionName);
		}

		return collection;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void insertTags(Document insertedDocument) {
		String tweet = insertedDocument.getString("name");
		getLogger().debug("inserting tags: {}", tweet);

		MongoCollection<Document> collection = getCollection(DatabaseConstants.TECHNIQUES_COLLECTION);
		collection.insertOne(insertedDocument);

		getLogger().trace("inserted tags: {}", tweet);
	}
}
