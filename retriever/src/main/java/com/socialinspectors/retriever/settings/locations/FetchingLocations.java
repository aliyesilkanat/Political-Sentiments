package com.socialinspectors.retriever.settings.locations;

import java.util.List;

import com.socialinspectors.retriever.data.FetchingLocationsLoader;

public class FetchingLocations {
	private static List<FetchingLocationsHolder> settings = null;

	private FetchingLocations() {
		settings = new FetchingLocationsLoader().loadCurrentSettings();
	}

	public static List<FetchingLocationsHolder> getSettings() {
		if (settings == null) {
			new FetchingLocations();
		}
		return settings;
	}
}
