package com.socialinspectors.analyzer.data;

import static org.junit.Assert.fail;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.socialinspectors.utils.data.DatabaseConstants;

public class AnalysisSettingsLoaderTest {
	private MongoDatabase mongoDatabase;

	@Before
	public void before() {
		mongoDatabase = MongoConnector.getDatabase();

	}

	@Test
	public void testSettingsCollectionHasOneDocument() throws Exception {

		// setup

		MongoCollection<Document> settingsCollection = mongoDatabase
				.getCollection(DatabaseConstants.SETTINGS_COLLECTION);

		// execute && assert
		if (settingsCollection.count() != 1) {
			fail();
		}
	}

	@Test
	public void testSettingsHasIntendedTagValue() throws Exception {
		// setup
		Document settingsDocument = new AnalysisSettingsLoader().getSettingsDocument(mongoDatabase);

		// execute
		FindIterable<Document> find = mongoDatabase.getCollection(DatabaseConstants.TECHNIQUES_COLLECTION)
				.find(new Document(CollectionConstants.ID, getCurrentTagId(settingsDocument)));

		// assert
		if (!find.iterator().hasNext()) {
			fail();
		}
	}

	private Integer getCurrentTagId(Document settingsDocument) {
		return settingsDocument.getInteger(CollectionConstants.CURRENT_TECHNIQUE_ID);
	}
}
