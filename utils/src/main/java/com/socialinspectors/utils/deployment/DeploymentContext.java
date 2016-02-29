package com.socialinspectors.utils.deployment;

import com.socialinspectors.utils.deployment.paths.GuardianPaths;

public class DeploymentContext {
	public static final String LOCALE = "locale";
	public static final String REMOTE = "remote";
	private DeploymentStrategy strategy;
	private String systemName;
	private String deployment;
	public DeploymentContext(String[] args, String systemName) {
		this.systemName = systemName;
		String deploy = REMOTE;
		if (args.length != 0) {
			deploy = args[0];
		}
		if (deploy.equals(LOCALE)) {
			setDeployment(LOCALE);
			strategy = new LocaleDeployment();
		} else {
			setDeployment(REMOTE);
			strategy = new RemoteDeployment();
		}
	}

	public GuardianPaths getGuardianPaths() {
		return strategy.getPaths();
	}

	public String getAkkaConfig() {
		return strategy.getAkkaConfig(systemName);
	}

	public String getDeployment() {
		return deployment;
	}

	public void setDeployment(String deployment) {
		this.deployment = deployment;
	}

}
