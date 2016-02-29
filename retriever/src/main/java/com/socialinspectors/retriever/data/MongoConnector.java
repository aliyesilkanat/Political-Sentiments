package com.socialinspectors.retriever.data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.utils.data.localisation.DatabaseLocalisationContext;

public class MongoConnector {
	private static MongoDatabase database = null;

	/**
	 * {@link MongoClient} client is a connection pool, not a single connection.
	 * So it's active always.
	 * 
	 * @return {@link MongoDatabase} database for "politicalsentiments"
	 */
	public static MongoDatabase getDatabase() {
		if (database == null) {
			database = new DatabaseLocalisationContext().createClient(RetrieverSystem.deploymentContext);
		}
		return database;
	}
}
