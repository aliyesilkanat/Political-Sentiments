package com.socialinspectors.analyzer.technique.senticnet;

import static org.junit.Assert.*;

import org.junit.Test;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class AdjectiveAndVerbCalculatorTest {
	@Test
	public void testNegOnVerb() throws Exception {
		AdjectiveAndVerbCalculator calculator = new AdjectiveAndVerbCalculator();
		CoreMap coreMap = getCoreMap("I do not like you.");
		AnalysisResultHolder result = calculator.getSentiment(coreMap);
		assertEquals(true, result.getVerbPolarity() < 0);

	}

	private CoreMap getCoreMap(String text) {
		return CoreNlpPipeline.getPipeline().process(text).get(CoreAnnotations.SentencesAnnotation.class).get(0);
	}
}
