package com.socialinspectors.analyzer.settings;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.socialinspectors.analyzer.data.CollectionConstants;

public class AnalysisSettingsTest {
	@Test
	public void testAnalysisSettings() throws Exception {
		String json = AnalysisSettings.getSettings().toJson();
		JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
		if (!jsonObject.has(CollectionConstants.ID)) {
			fail();
		}
		if (!jsonObject.has(CollectionConstants.DESCRIPTION)) {
			fail();
		}
		if (!jsonObject.has(CollectionConstants.NAME)) {
			fail();
		}
	}

}
