package com.socialinspectors.utils.deployment.paths;

import com.socialinspectors.utils.deployment.constants.GuardianActorNames;
import com.socialinspectors.utils.deployment.constants.RemoteIPs;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.deployment.constants.SystemPorts;

public class RemoteGuardianPaths implements GuardianPaths {

	private String createPath(String systemName, int port, String guardianActor, String IP) {
		return "akka.tcp://" + systemName + "@" + IP + ":" + port + "/user/" + guardianActor;
	}

	@Override
	public String getAnalyzer() {
		return createPath(SystemNames.ANALYZER, SystemPorts.ANALYZER, GuardianActorNames.ANALYZER_GUARDIAN,
				RemoteIPs.ANALYZER);
	}

	@Override
	public String getRetriever() {
		return createPath(SystemNames.RETRIEVER, SystemPorts.RETRIEVER, GuardianActorNames.RETRIEVER_GUARDIAN,
				RemoteIPs.RETRIEVER);
	}

	@Override
	public String getStorer() {
		return createPath(SystemNames.STORER, SystemPorts.STORER, GuardianActorNames.STORER_GUARDIAN, RemoteIPs.STORER);
	}

	@Override
	public String getTrigger() {
		return createPath(SystemNames.TRIGGER, SystemPorts.TRIGGER, GuardianActorNames.TRIGGER_GUARDIAN,
				RemoteIPs.TRIGGER);
	}
}
