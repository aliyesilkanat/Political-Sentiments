package com.socialinspectors.analyzer.technique.pipeline;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentenceAnalysisPipelineTest {
	@Test
	public void testPipeline() throws Exception {
		CoreMap coreMap = CoreNlpPipeline.getPipeline().process("I love you.")
				.get(CoreAnnotations.SentencesAnnotation.class).get(0);
		assertEquals(0.01438, new SentenceAnalysisPipeline().extractSentiment(coreMap), 0.00001);

	}
}
