package com.socialinspectors.analyzer.technique;

import org.junit.Test;

public class TechniqueContextTest {
	private static final int UNAVAILABLE_TAG_NUMBER = 1;

	@Test(expected = UnexpectedTagIdException.class)
	public void testUnexpectedTagId() throws Exception {
		new TechniqueContext(-UNAVAILABLE_TAG_NUMBER);
	}
}
