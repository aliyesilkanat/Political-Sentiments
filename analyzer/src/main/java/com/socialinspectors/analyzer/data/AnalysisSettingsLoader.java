package com.socialinspectors.analyzer.data;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;
import com.socialinspectors.analyzer.settings.AnalysisSettingsHolder;
import com.socialinspectors.data.DatabaseConstants;
import com.socialinspectors.utils.OntologyProperties;

import edu.stanford.nlp.patterns.Data;

public class AnalysisSettingsLoader {

	public AnalysisSettingsHolder loadCurrentSettings() {
		MongoDatabase mongoDatabase = MongoConnector.getDatabase();
		Document settingsDocument = getSettingsDocument(mongoDatabase);
		Document tagDocument = getTagsDocument(mongoDatabase, settingsDocument);

		AnalysisSettingsHolder holder = new AnalysisSettingsHolder();
		holder.loadFromMongoDocument(settingsDocument, tagDocument);
		return holder;
	}

	Document getTagsDocument(MongoDatabase mongoDatabase, Document settingsDocument) {
		return mongoDatabase.getCollection(DatabaseConstants.TECHNIQUES_COLLECTION)
				.find(filterByTagId(settingsDocument)).first();
	}

	Document getSettingsDocument(MongoDatabase mongoDatabase) {
		return mongoDatabase.getCollection(DatabaseConstants.SETTINGS_COLLECTION).find().first();
	}

	Document filterByTagId(Document settingsDocument) {
		return new Document(CollectionConstants.ID, settingsDocument.getInteger(CollectionConstants.CURRENT_TECHNIQUE_ID));
	}
}
