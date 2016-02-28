package com.socialinspectors.retriever.settings.locations;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.google.gson.Gson;
import com.socialinspectors.utils.OntologyProperties;

public class FetchingLocationsHolder {
	private static final Logger logger = LogManager.getLogger(FetchingLocationsHolder.class);

	public static Logger getLogger() {
		return logger;
	}

	private String city;
	private double latitude;

	private double longitude;

	private String state;

	public String getCity() {
		return city;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getState() {
		return state;
	}

	private void loadCity(Document document) {
		if (document.containsKey(OntologyProperties.CITY)) {
			setCity(document.getString(OntologyProperties.CITY));
		} else {
			getLogger().warn("not found city element in document: {}", document.toJson());
		}
	}

	/**
	 * Loads class fields from given {@link Document} object.
	 * 
	 * @param document
	 *            settings document fetched from db.
	 */
	public void loadFromMongoDocument(Document document) {
		getLogger().info("loading location information from given document: {}", document.toJson());
		loadCity(document);
		loadState(document);
		setLatitude(document.getDouble(OntologyProperties.LATITUDE));
		setLongitude(document.getDouble(OntologyProperties.LONGITUDE));
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("loaded location information from given document: {}", document.toJson());
		}
	}

	private void loadState(Document document) {
		if (document.containsKey(OntologyProperties.STATE)) {
			setState(document.getString(OntologyProperties.STATE));
		} else {
			getLogger().warn("not found state element in document: {}", document.toJson());
		}
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String toJson() {
		return new Gson().toJson(this);
	}
}
