package com.socialinspectors.deployment.paths;

import com.socialinspectors.deployment.constants.GuardianActorNames;
import com.socialinspectors.deployment.constants.SystemNames;
import com.socialinspectors.deployment.constants.SystemPorts;

public class LocaleGuardianPaths implements GuardianPaths {

	private static final String LOCALE_IP = "127.0.0.1";

	private String createPath(String systemName, int port, String guardianActor) {
		return "akka.tcp://" + systemName + "@" + LOCALE_IP + ":" + port
				+ "/user/" + guardianActor;
	}

	@Override
	public String getAnalyzer() {
		return createPath(SystemNames.ANALYZER, SystemPorts.ANALYZER,
				GuardianActorNames.ANALYZER_GUARDIAN);
	}

	@Override
	public String getRetriever() {
		return createPath(SystemNames.RETRIEVER, SystemPorts.RETRIEVER,
				GuardianActorNames.RETRIEVER_GUARDIAN);
	}

	@Override
	public String getStorer() {
		return createPath(SystemNames.STORER, SystemPorts.STORER,
				GuardianActorNames.STORER_GUARDIAN);
	}

	@Override
	public String getTrigger() {
		return createPath(SystemNames.TRIGGER, SystemPorts.TRIGGER,
				GuardianActorNames.TRIGGER_GUARDIAN);
	}
}
