package com.socialinspectors.analyzer.technique;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class OpenNLPCategorizerTest {
	private OpenNLPCategorizer openNLPCategorizer;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testCategorizing() throws Exception {

		openNLPCategorizer = new OpenNLPCategorizer();
		int analyze = openNLPCategorizer.analyze("No but really, why");
		assertEquals(AnalysisResultCodes.NEGATIVE, analyze);
	}
}
