package com.socialinspectors.deployment;

import com.socialinspectors.deployment.paths.GuardianPaths;

public class DeploymentContext {
	public static final String LOCALE = "locale";
	public static final String REMOTE = "remote";
	private DeploymentStrategy strategy;
	private String systemName;

	public DeploymentContext(String[] args, String systemName) {
		this.systemName = systemName;
		String deploy = REMOTE;
		if (args.length != 0) {
			deploy = args[0];
		}
		if (deploy.equals(LOCALE)) {
			strategy = new LocaleDeployment();
		} else {
			strategy = new RemoteDeployment();
		}
	}

	public GuardianPaths getGuardianPaths() {
		return strategy.getPaths();
	}

	public String getAkkaConfig() {
		return strategy.getAkkaConfig(systemName);
	}

}
