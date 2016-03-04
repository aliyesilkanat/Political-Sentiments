package com.socialinspectors.analyzer;

import static org.junit.Assert.*;

import org.junit.Test;

public class SenticnetClientTest {
	private static final double HAPPY_POLARITY = 0.298;
	private static final String HAPPY_CONCEPT_XML = "<rdf:RDF xmlns:rdf=\"http://w3.org/1999/02/22-rdf-syntax-ns#\">\r\n<rdf:Description rdf:about=\"http://sentic.net/api/en/concept/happy/polarity\">\r\n<rdf:type rdf:resource=\"http://sentic.net/api/concept/polarity\"/>\r\n<polarity xmlns=\"http://sentic.net\" rdf:datatype=\"http://w3.org/2001/XMLSchema#float\">0.298</polarity>\r\n</rdf:Description>\r\n</rdf:RDF>";

	@Test
	public void testGetConceptDocument() throws Exception {
		double conceptPolarity = new SenticnetClient().getConceptPolarity("happy");
		assertEquals(HAPPY_POLARITY, conceptPolarity, 0.00001);
	}

	@Test
	public void testExtractPolarity() throws Exception {
		double polarity = new SenticnetClient().extractPolarity(HAPPY_CONCEPT_XML, "happy");
		assertEquals(0.298, polarity, 0.00001);

	}
}
