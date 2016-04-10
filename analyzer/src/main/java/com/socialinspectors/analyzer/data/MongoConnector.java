package com.socialinspectors.analyzer.data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.socialinspectors.analyzer.AnalyzerSystem;
import com.socialinspectors.utils.data.localisation.DatabaseLocalisationContext;

public class MongoConnector {
	private static MongoDatabase database = null;

	/**
	 * {@link MongoClient} client is a connection pool, not a single connection.
	 * So it's active always.
	 * 
	 * @return {@link MongoDatabase} database for "politicsentiments"
	 */
	public static MongoDatabase getDatabase() {
		if (database == null) {
			database = new DatabaseLocalisationContext().createClient(AnalyzerSystem.deploymentContext);
		}
		return database;
	}
}
