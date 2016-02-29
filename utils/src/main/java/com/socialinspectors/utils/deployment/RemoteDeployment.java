package com.socialinspectors.utils.deployment;

import com.socialinspectors.utils.deployment.constants.RemoteIPs;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.deployment.constants.SystemPorts;
import com.socialinspectors.utils.deployment.paths.GuardianPaths;
import com.socialinspectors.utils.deployment.paths.RemoteGuardianPaths;

public class RemoteDeployment extends DeploymentStrategy {

	private RemoteGuardianPaths paths;

	public RemoteDeployment() {
		paths = new RemoteGuardianPaths();
	}

	@Override
	public GuardianPaths getPaths() {
		return paths;
	}

	@Override
	public String getAkkaConfig(String systemName) {
		int port = 8090;
		String ip = "127.0.0.1";
		if (systemName.equals(SystemNames.TRIGGER)) {
			ip = RemoteIPs.TRIGGER;
			port = SystemPorts.TRIGGER;
		} else if (systemName.equals(SystemNames.STORER)) {
			ip = RemoteIPs.STORER;
			port = SystemPorts.STORER;
		} else if (systemName.equals(SystemNames.RETRIEVER)) {
			ip = RemoteIPs.RETRIEVER;
			port = SystemPorts.RETRIEVER;
		} else if (systemName.equals(SystemNames.ANALYZER)) {
			ip = RemoteIPs.ANALYZER;
			port = SystemPorts.ANALYZER;
		}

		return createAkkaConfig(ip, port);
	}
}