package com.socialinspectors.analyzer.model;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class PerceptronSingleton {
	private Logger logger = LogManager.getLogger(PerceptronSingleton.class);
	private MultiLayerNetwork network = null;
	private static PerceptronSingleton instance = null;

	private PerceptronSingleton() {
		try {
			network = new AdjAndVerbMLP().train();
			getLogger().info("trained multi layer network succesfully");
		} catch (IOException | InterruptedException e) {
			getLogger().fatal("cannot train multi layer network", e);
		}
	}

	public MultiLayerNetwork getNetwork() {

		return network;
	}

	public static PerceptronSingleton getInstance() {
		if (instance == null) {
			instance = new PerceptronSingleton();
		}
		return instance;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
