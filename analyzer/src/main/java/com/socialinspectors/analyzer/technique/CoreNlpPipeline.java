package com.socialinspectors.analyzer.technique;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class CoreNlpPipeline {

	private static StanfordCoreNLP pipeline = null;

	private CoreNlpPipeline() {
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, parse, depparse,lemma");
		pipeline = new StanfordCoreNLP(properties);
	}

	public static StanfordCoreNLP getPipeline() {
		if (pipeline == null) {
			new CoreNlpPipeline();
		}

		return pipeline;
	}
}
