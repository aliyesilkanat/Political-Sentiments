package com.socialinspectors.analyzer.technique.pipeline;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SentimentAnalysisPipeline {
	private static final Logger logger = LogManager.getLogger(SentimentAnalysisPipeline.class);

	public static Logger getLogger() {
		return logger;
	}
}
