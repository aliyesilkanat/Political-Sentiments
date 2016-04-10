package com.socialinspectors.utils.data.localisation;

import com.mongodb.client.MongoDatabase;
import com.socialinspectors.utils.data.DatabaseConstants;
import com.socialinspectors.utils.deployment.DeploymentContext;

public class DatabaseLocalisationContext {
	public MongoDatabase createClient(DeploymentContext deploymentContext) {

		LocalisationStrategy strategy;
		if (deploymentContext.getDeployment().equals(DeploymentContext.LOCALE)) {
			strategy = new LocalLocalisation();
		} else {
			strategy = new RemoteLocalisation();
		}
		return strategy.getClient().getDatabase(DatabaseConstants.DATABASE);
	}
}
