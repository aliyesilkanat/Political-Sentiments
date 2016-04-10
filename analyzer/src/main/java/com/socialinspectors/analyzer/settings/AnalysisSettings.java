package com.socialinspectors.analyzer.settings;

import com.socialinspectors.analyzer.data.AnalysisSettingsLoader;


public class AnalysisSettings {
	private static AnalysisSettingsHolder settings = null;

	private AnalysisSettings() {
		settings = new AnalysisSettingsLoader().loadCurrentSettings();
	}

	public static AnalysisSettingsHolder getSettings() {
		if (settings == null) {
			new AnalysisSettings();
		}
		return settings;
	}

}
