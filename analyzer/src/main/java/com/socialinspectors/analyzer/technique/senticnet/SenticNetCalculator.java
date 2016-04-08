package com.socialinspectors.analyzer.technique.senticnet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.technique.TechniqueStrategy;

public abstract class SenticNetCalculator implements TechniqueStrategy {
	private static final Logger logger = LogManager.getLogger(SenticNetCalculator.class);

	@Override
	public int analyze(String tweet) {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("analyzing tweet: {}", tweet);
		}
		double mainSentiment;
		try {
			mainSentiment = getSentiment(tweet);
		} catch (Exception e) {
			mainSentiment = -1;
			getLogger().fatal("could not got sentiment", e);
		}
		if (getLogger().isTraceEnabled()) {
			getLogger().trace("analyzed tweet. result: {}, tweet: {}", mainSentiment, tweet);
		}
		// TODO float or integer? Respecting to TechniqueStrategy Interface
		return (int) Math.round(mainSentiment);
	}

	public abstract double getSentiment(String tweet) throws Exception;

	public static Logger getLogger() {
		return logger;
	}

}
