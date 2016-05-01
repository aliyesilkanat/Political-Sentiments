package com.socialinspectors.analyzer.technique.pipeline;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.socialinspectors.analyzer.model.AdjAndVerbMLP;
import com.socialinspectors.analyzer.model.MLPOutputNormalizer;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveAndVerbCalculator;
import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;
import com.socialinspectors.analyzer.technique.senticnet.SentenceSplitter;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentenceAnalysisPipeline {
	private static final Logger logger = LogManager.getLogger(SentenceAnalysisPipeline.class);

	public double extractSentiment(CoreMap sentence) throws Exception {
		String split = splitSentence(sentence);
		CoreMap splittedSent = sentence;
		if (split != null) {
			// exception and empty sentence check
			splittedSent = CoreNlpPipeline.getPipeline().process(split).get(CoreAnnotations.SentencesAnnotation.class)
					.get(0);
		}
		AnalysisResultHolder result = new AdjectiveAndVerbCalculator().getSentiment(splittedSent);
		INDArray output = AdjAndVerbMLP.getModel().output(result.getINDArray());
		double normalize = new MLPOutputNormalizer().normalize(result, output.getDouble(1) - 0.5);
		getLogger().info("calculated sentence score: {}, sentence: {}", normalize, splittedSent.toString());
		return normalize;

	}

	private String splitSentence(CoreMap sentence) {
		String split = null;
		try {
			split = new SentenceSplitter().split(sentence);
		} catch (Exception e) {
			getLogger().error("cannot split sentence: {}", sentence);
		}
		return split;
	}

	public static Logger getLogger() {
		return logger;
	}
}
