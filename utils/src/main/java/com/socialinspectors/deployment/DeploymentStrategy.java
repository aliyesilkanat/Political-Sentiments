package com.socialinspectors.deployment;

import com.socialinspectors.deployment.paths.GuardianPaths;

public abstract class DeploymentStrategy {
	private static final String LOG_LEVEL = "INFO";

	public abstract GuardianPaths getPaths();

	public abstract String getAkkaConfig(String systemName);

	protected String createAkkaConfig(String ip, int port) {
		return "akka {\r\n  loglevel = \""
				+ LOG_LEVEL
				+ "\"\r\n  actor {\r\n    provider = \"akka.remote.RemoteActorRefProvider\"\r\n  }\r\n  remote {\r\n\r\n    enabled-transports = [\"akka.remote.netty.tcp\"]\r\n    netty.tcp {\r\n      hostname = \""
				+ ip + "\"\r\n      port = " + port
				+ "\r\n    } \r\n    \r\n }\r\n}";
	}
}
