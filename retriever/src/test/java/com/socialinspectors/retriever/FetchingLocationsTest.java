package com.socialinspectors.retriever;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.socialinspectors.retriever.settings.locations.FetchingLocations;
import com.socialinspectors.utils.OntologyProperties;

public class FetchingLocationsTest {
	@Test
	public void testSettingsLoad() throws Exception {
		String json = FetchingLocations.getSettings().get(0).toJson();
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		if (jsonObject.has(OntologyProperties.CITY)
				&& jsonObject.get(OntologyProperties.CITY).isJsonNull()) {
			fail("Settings has not city property");
		}
		if (jsonObject.has(OntologyProperties.STATE)
				&& jsonObject.get(OntologyProperties.STATE).isJsonNull()) {
			fail("Settings has not state property");
		}
		if ((jsonObject.has(OntologyProperties.LONGITUDE)
				&& jsonObject.get(OntologyProperties.LONGITUDE).isJsonNull())) {
			try {
				jsonObject.get(OntologyProperties.LONGITUDE).getAsDouble();
			} catch (Exception e) {
				fail("Settings wrong longitude format");
			}
		}
		if ((jsonObject.has(OntologyProperties.LATITUDE)
				&& jsonObject.get(OntologyProperties.LATITUDE).isJsonNull())) {
			try {
				jsonObject.get(OntologyProperties.LATITUDE).getAsDouble();
			} catch (Exception e) {
				fail("Settings wrong latitude format");
			}
		}

	}
}
