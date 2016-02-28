package com.socialinspectors.retriever.data;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.socialinspectors.data.DatabaseConstants;
import com.socialinspectors.retriever.settings.users.FetchingPeopleHolder;

public class FetchingPeopleLoader {

	private static final Logger logger = LogManager.getLogger(FetchingPeopleLoader.class);

	public HashMap<String, FetchingPeopleHolder> loadCurrentSettings() {
		MongoCollection<Document> collection = MongoConnector.getDatabase()
				.getCollection(DatabaseConstants.FETCHING_PEOPLE_COLLECTION);

		logger.info("loading politic people, people count in database: {}", collection.count());
		FindIterable<Document> documents = collection.find();
		HashMap<String, FetchingPeopleHolder> locationsList = loadPeopleMap(documents);
		return locationsList;
	}

	/**
	 * Load people with given {@link FindIterable} Mongo iterable.
	 * 
	 * @param documents
	 *            from "fetchim_people" collection in MongoDb.
	 * @return Loaded people {@link ArrayList}
	 */
	private HashMap<String, FetchingPeopleHolder> loadPeopleMap(FindIterable<Document> documents) {
		HashMap<String, FetchingPeopleHolder> peopleMap = new HashMap<String, FetchingPeopleHolder>();

		MongoCursor<Document> iterator = documents.iterator();
		while (iterator.hasNext()) {
			// load each people document.

			Document document = iterator.next();
			FetchingPeopleHolder holder = new FetchingPeopleHolder();
			String userName = holder.loadFromMongoDocument(document);
			peopleMap.put(userName, holder);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loaded politic people, count: {}", peopleMap.size());
		}
		return peopleMap;
	}
}
