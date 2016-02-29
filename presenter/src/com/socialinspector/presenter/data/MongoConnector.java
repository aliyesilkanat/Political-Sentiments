package com.socialinspector.presenter.data;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.socialinspector.presenter.ServletInitializer;
import com.socialinspectors.utils.data.localisation.DatabaseLocalisationContext;

public class MongoConnector {
	private static MongoDatabase database = null;

	/**
	 * {@link MongoClient} client is a connection pool, not a single connection.
	 * So it's active always.
	 * 
	 * @return {@link MongoDatabase} database for "social_inspectors"
	 */
	public static MongoDatabase getDatabase() {
		if (database == null) {
			database = new DatabaseLocalisationContext().createClient(ServletInitializer.context);
		}
		return database;
	}
}
