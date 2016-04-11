package com.socialinspectors.analyzer.technique.senticnet;

import static org.junit.Assert.*;

import org.junit.Test;

public class DependencyCalculatorTest {

	@Test
	public void testSentiment() throws Exception {
		String sentence1 = "Meal was good however, it was cold.";
		String sentence2 = "She is doing an amazing job.";
		String sentence3 = "I do not like genetically modified food.";
		DependencyCalculator calculator = new DependencyCalculator();
		// calculator.getSentiment("A nice looking young woman did an amazing
		// job.");
		// calculator.analyze("The meal was not good and delicious.");
		// calculator.analyze(sentence1);
		// calculator.analyze(sentence2);
		// calculator.analyze(sentence3);
		calculator.analyze("Big hug and lots of love.");

	}

}
