package com.socialinspectors.analyzer.settings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.socialinspectors.analyzer.data.CollectionConstants;

public class AnalysisSettingsHolder {

	private static final Logger logger = LogManager
			.getLogger(AnalysisSettingsHolder.class);

	public static Logger getLogger() {
		return logger;
	}

	private int id;
	private String name;
	private String description;

	/**
	 * Loads class fields from given {@link Document} objects.
	 * 
	 * @param settingsDocument
	 *            settings document fetched from db.
	 * @param tagDocument
	 *            tags document fetched from db.
	 */
	public void loadFromMongoDocument(Document settingsDocument,
			Document tagDocument) {
		getLogger().info("loading settings from given document: {}",
				settingsDocument.toJson());
		setId(settingsDocument.getInteger(CollectionConstants.CURRENT_TECHNIQUE_ID));
		setDescription(tagDocument.getString(CollectionConstants.DESCRIPTION));
		setName(tagDocument.getString(CollectionConstants.NAME));
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("loaded settings from given document: {}",
					settingsDocument.toJson());
		}
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
