package com.socialinspectors.retriever.settings.users;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.socialinspectors.utils.OntologyProperties;

public class FetchingPeopleHolder {

	private static final Logger logger = LogManager.getLogger(FetchingPeopleHolder.class);

	public static Logger getLogger() {
		return logger;
	}

	private String fullName;
	private String politics;
	private String twitterName;

	private String twitterUrl;

	public String getFullName() {
		return fullName;
	}

	public String getPolitics() {
		return politics;
	}

	public String getTwitterName() {
		return twitterName;
	}

	public String getTwitterUrl() {
		return twitterUrl;
	}

	/**
	 * Loads class fields from given {@link Document} object.
	 * 
	 * @param document
	 *            settings document fetched from db.
	 * 
	 * @return given person's full name in order to put it into {@link HashMap}.
	 */
	public String loadFromMongoDocument(Document document) {
		getLogger().info("loading person information from given document: {}", document.toJson());
		setPolitics(document.getString(OntologyProperties.POLITICS));
		setFullName(document.getString(OntologyProperties.FULL_NAME));
		setTwitterName(document.getString(OntologyProperties.TWITTER_NAME));
		setTwitterUrl(document.getString(OntologyProperties.TWITTER_URL));

		if (getLogger().isTraceEnabled()) {
			getLogger().trace("loaded person information from given document: {}", document.toJson());
		}

		return getTwitterName();

	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setPolitics(String politics) {
		this.politics = politics;
	}

	public void setTwitterName(String twitterName) {
		this.twitterName = twitterName;
	}

	public void setTwitterUrl(String twitterUrl) {
		this.twitterUrl = twitterUrl;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
}
