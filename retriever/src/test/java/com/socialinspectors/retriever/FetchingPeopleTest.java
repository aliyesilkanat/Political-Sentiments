package com.socialinspectors.retriever;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.socialinspectors.retriever.settings.users.FetchingPeople;
import com.socialinspectors.utils.OntologyProperties;

public class FetchingPeopleTest {
	@Test
	public void testPeopleMap() throws Exception {

		String json = FetchingPeople.getSettings().values().iterator().next().toJson();
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		if (jsonObject.has(OntologyProperties.TWITTER_NAME)
				&& jsonObject.get(OntologyProperties.TWITTER_NAME).isJsonNull()) {
			fail("Person has not twitter name property");
		}
		if (jsonObject.has(OntologyProperties.TWITTER_URL)
				&& jsonObject.get(OntologyProperties.TWITTER_URL).isJsonNull()) {
			fail("Person has not twitter url property");
		}
		if ((jsonObject.has(OntologyProperties.FULL_NAME)
				&& jsonObject.get(OntologyProperties.FULL_NAME).isJsonNull())) {
			fail("Person has not full name property");
		}
		if ((jsonObject.has(OntologyProperties.POLITICS) && jsonObject.get(OntologyProperties.POLITICS).isJsonNull())) {
			fail("Person has not politics property");
		}

	}

}
