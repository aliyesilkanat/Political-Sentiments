package com.socialinspectors.integration;

import com.socialinspectors.analyzer.AnalyzerSystem;
import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.storer.StorerSystem;
import com.socialinspectors.utils.DeploymentConstants;

public class RunSystems {
	private static final String[] LOCALE = new String[] { DeploymentConstants.LOCALE };

	public static void main(String[] args) {
		StorerSystem.main(LOCALE);
		RetrieverSystem.main(LOCALE);
		AnalyzerSystem.main(LOCALE);

	}
}
