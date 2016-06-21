package com.socialinspectors.analyzer.technique.pipeline;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.technique.TechniqueStrategy;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class TextAnalysisPipeline implements TechniqueStrategy {
	private static final Logger logger = LogManager.getLogger(TextAnalysisPipeline.class);

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public int analyze(String tweet) {

		ConcurrentLinkedQueue<Double> sentencesPolarity = new ConcurrentLinkedQueue<Double>();
		List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(tweet)
				.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentenceMap : sentences) {
			sentencesPolarity.add(new SentenceAnalysisPipeline().extractSentiment(sentenceMap));
		}
		// get the mean of the sentences polarities
		double tweetPolarity = sentencesPolarity.stream().mapToDouble(val -> val).average().getAsDouble();
		getLogger().trace("found tweet polarity, score: {}, tweet: {}", tweetPolarity, tweet);
		if (tweetPolarity < 0) { //below than zero means negative
			return 0;
		} else { //above than zero means positive
			return 1;
		}
	}
}
