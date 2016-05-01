package com.socialinspectors.analyzer.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;

public class MLPOutputNormalizerTest {
	private static final double POSITIVE_POLARITY = 0.3;
	private static final double NEGATIVE_POLARITY = -0.3;
	private static final double ZERO_POLARITY = 0;
	private MLPOutputNormalizer normalizer;

	@Before
	public void before() {
		normalizer = new MLPOutputNormalizer();

	}

	@Test
	public void testNormalize() throws Exception {
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, POSITIVE_POLARITY),
				POSITIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, POSITIVE_POLARITY),
				NEGATIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, NEGATIVE_POLARITY),
				POSITIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, NEGATIVE_POLARITY),
				NEGATIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(NEGATIVE_POLARITY, POSITIVE_POLARITY),
				POSITIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(NEGATIVE_POLARITY, POSITIVE_POLARITY),
				NEGATIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(NEGATIVE_POLARITY, NEGATIVE_POLARITY),
				NEGATIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(NEGATIVE_POLARITY, NEGATIVE_POLARITY),
				POSITIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(ZERO_POLARITY, POSITIVE_POLARITY),
				POSITIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(ZERO_POLARITY, POSITIVE_POLARITY),
				NEGATIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(ZERO_POLARITY, NEGATIVE_POLARITY),
				POSITIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(ZERO_POLARITY, NEGATIVE_POLARITY),
				NEGATIVE_POLARITY) < 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, ZERO_POLARITY),
				POSITIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(POSITIVE_POLARITY, ZERO_POLARITY),
				NEGATIVE_POLARITY) > 0);
		assertEquals(true, normalizer.normalize(new AnalysisResultHolder(NEGATIVE_POLARITY, ZERO_POLARITY),
				POSITIVE_POLARITY) < 0);

	}
}
