package com.socialinspectors.retriever.data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.socialinspectors.data.DatabaseConstants;

public class MongoConnector {
	private static MongoClient client = null;
	

	/**
	 * {@link MongoClient} client is a connection pool, not a single connection.
	 * So it's active always.
	 * 
	 * @return {@link MongoDatabase} database for "social_inspectors"
	 */
	public static MongoDatabase getDatabase() {
		if (client == null) {
			client = new MongoClient(DatabaseConstants.MONGODB_IP, DatabaseConstants.MONGODB_PORT);

		}
		return client.getDatabase(DatabaseConstants.DATABASE);
	}
}
