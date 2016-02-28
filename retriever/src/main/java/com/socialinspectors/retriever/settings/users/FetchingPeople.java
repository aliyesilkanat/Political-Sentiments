package com.socialinspectors.retriever.settings.users;

import java.util.HashMap;

import com.socialinspectors.retriever.data.FetchingPeopleLoader;

public class FetchingPeople {
	private static HashMap<String, FetchingPeopleHolder> settings = null;

	private FetchingPeople() {
		settings = new FetchingPeopleLoader().loadCurrentSettings();
	}

	public static HashMap<String, FetchingPeopleHolder> getSettings() {
		if (settings == null) {
			new FetchingPeople();
		}
		return settings;
	}
}
