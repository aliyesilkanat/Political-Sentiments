package com.socialinspectors.integration;

import com.socialinspectors.analyzer.AnalyzerSystem;
import com.socialinspectors.retriever.RetrieverSystem;
import com.socialinspectors.storer.StorerSystem;
import com.socialinspectors.utils.prop.DeploymentConstants;

public class RunSystems {
	private static final String[] LOCALE = new String[] { DeploymentConstants.LOCALE };
	private static final String[] REMOTE = new String[] { DeploymentConstants.REMOTE };

	public static void main(String[] args) {
		String[] environment = REMOTE;
		StorerSystem.main(environment);
		RetrieverSystem.main(environment);
		AnalyzerSystem.main(environment);

	}
}
