package com.socialinspectors.utils.data.localisation;

import com.mongodb.MongoClient;

public interface LocalisationStrategy {
	public MongoClient getClient();
}
