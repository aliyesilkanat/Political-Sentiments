package com.socialinspectors.utils.data.localisation;

import com.mongodb.MongoClient;
import com.socialinspectors.utils.data.DatabaseConstants;

public class LocalLocalisation implements LocalisationStrategy {

	@Override
	public MongoClient getClient() {
		return new MongoClient(DatabaseConstants.MONGODB_IP, DatabaseConstants.MONGODB_PORT);
	}
}
