package com.socialinspectors.analyzer.technique;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class CoreNLPSentiment implements TechniqueStrategy {
	private final Logger logger = LogManager.getLogger(getClass());
	private final String[] emotions = { "very negative", "negative", "neutral",
			"positive", "very positive" };

	@Override
	public int analyze(String tweet) {
		getLogger().debug("analyzing tweet: {}", tweet);
		int mainSentiment = getSentiment(tweet);
		getLogger().trace("analyzed tweet. result: {}, tweet: {}",
				emotions[mainSentiment], tweet);
		mainSentiment = convertResultIds(mainSentiment);

		return mainSentiment;
	}

	/**
	 * Converts result ids of coreNlp sentiment to system result ids.
	 * 
	 * @param mainSentiment
	 *            CoreNlp Sentiment result Id.
	 * @return System Result Id.
	 */
	private int convertResultIds(int mainSentiment) {
		if (mainSentiment == 0) {
			mainSentiment = AnalysisResultCodes.VERY_NEGATIVE;
		} else if (mainSentiment == 1) {
			mainSentiment = AnalysisResultCodes.NEGATIVE;
		} else if (mainSentiment == 2) {
			mainSentiment = AnalysisResultCodes.NEUTRAL;
		} else if (mainSentiment == 3) {
			mainSentiment = AnalysisResultCodes.POSITIVE;
		} else if (mainSentiment == 4) {
			mainSentiment = AnalysisResultCodes.VERY_POSITIVE;
		}
		return mainSentiment;
	}

	/**
	 * Gets sentiment using RNN.
	 * 
	 * @param tweet
	 *            Tweet which will sentiment extracted.
	 * @return sentiment result.
	 */
	private int getSentiment(String tweet) {
		StanfordCoreNLP pipeline = createPipeline();
		int mainSentiment = 0;
		int longest = 0;
		Annotation annotation = pipeline.process(tweet);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentAnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			String partText = sentence.toString();
			if (partText.length() > longest) {
				mainSentiment = sentiment;
				longest = partText.length();
			}
		}
		return mainSentiment;
	}

	private StanfordCoreNLP createPipeline() {
		Properties properties = new Properties();
		properties.setProperty("annotators",
				"tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
		return pipeline;
	}

	public Logger getLogger() {
		return logger;
	}

}
