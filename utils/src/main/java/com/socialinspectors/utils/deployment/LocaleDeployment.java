package com.socialinspectors.utils.deployment;

import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.deployment.constants.SystemPorts;
import com.socialinspectors.utils.deployment.paths.GuardianPaths;
import com.socialinspectors.utils.deployment.paths.LocaleGuardianPaths;

public class LocaleDeployment extends DeploymentStrategy {

	private LocaleGuardianPaths paths;

	public LocaleDeployment() {
		paths = new LocaleGuardianPaths();
	}

	@Override
	public GuardianPaths getPaths() {
		return paths;
	}

	@Override
	public String getAkkaConfig(String systemName) {
		String ip = "127.0.0.1";
		int port = 8090;
		if (systemName.equals(SystemNames.TRIGGER)) {
			port = SystemPorts.TRIGGER;
		} else if (systemName.equals(SystemNames.STORER)) {
			port = SystemPorts.STORER;
		} else if (systemName.equals(SystemNames.RETRIEVER)) {
			port = SystemPorts.RETRIEVER;
		} else if (systemName.equals(SystemNames.ANALYZER)) {
			port = SystemPorts.ANALYZER;
		}
		return createAkkaConfig(ip, port);
	}

}
