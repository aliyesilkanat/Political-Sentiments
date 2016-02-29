package com.socialinspectors.utils.data.localisation;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class RemoteLocalisation implements LocalisationStrategy {

	private static final String MONGOLAB_PORT = "19048";
	private static final String MONGOLAB_IP = "ds019048.mlab.com";
	private static final String MONGOLAB_PASSWORD = "121121121";
	private static final String MONGOLAB_USER = "admin";

	@Override
	public MongoClient getClient() {
		return new MongoClient(new MongoClientURI("mongodb://" + MONGOLAB_USER + ":" + MONGOLAB_PASSWORD + "@"
				+ MONGOLAB_IP + ":" + MONGOLAB_PORT + "/politicalsentiments"));

	}
}
