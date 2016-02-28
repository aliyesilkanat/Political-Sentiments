package com.socialinspectors.retriever.data;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.socialinspectors.data.DatabaseConstants;
import com.socialinspectors.retriever.settings.locations.FetchingLocationsHolder;

public class FetchingLocationsLoader {
	/**
	 * Loads locations from MongoDb.
	 * 
	 * @return loaded locations.
	 */
	private static final Logger logger = LogManager.getLogger(FetchingLocationsLoader.class);

	public ArrayList<FetchingLocationsHolder> loadCurrentSettings() {
		MongoCollection<Document> collection = MongoConnector.getDatabase()
				.getCollection(DatabaseConstants.FETCHING_LOCATIONS_COLLECTION);

		logger.info("loading locations, locations count in database: {}", collection.count());
		FindIterable<Document> documents = collection.find();
		ArrayList<FetchingLocationsHolder> locationsList = loadLocationsArray(documents);
		return locationsList;
	}

	/**
	 * Load locations with given {@link FindIterable} Mongo iterable.
	 * 
	 * @param documents
	 *            from "fetching_locations" collection in MongoDb.
	 * @return Loaded locations {@link ArrayList}
	 */
	private ArrayList<FetchingLocationsHolder> loadLocationsArray(FindIterable<Document> documents) {
		ArrayList<FetchingLocationsHolder> locationsList = new ArrayList<FetchingLocationsHolder>();

		MongoCursor<Document> iterator = documents.iterator();
		while (iterator.hasNext()) {
			// load each location document.

			Document document = iterator.next();
			FetchingLocationsHolder holder = new FetchingLocationsHolder();
			holder.loadFromMongoDocument(document);
			locationsList.add(holder);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loaded locations, count: {}", locationsList.size());
		}
		return locationsList;
	}

	public static Logger getLogger() {
		return logger;
	}
}
