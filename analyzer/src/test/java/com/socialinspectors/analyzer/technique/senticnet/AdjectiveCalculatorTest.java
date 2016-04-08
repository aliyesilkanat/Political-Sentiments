package com.socialinspectors.analyzer.technique.senticnet;

import static org.junit.Assert.*;

import org.junit.Test;

import com.socialinspectors.analyzer.technique.senticnet.AdjectiveCalculator;

public class AdjectiveCalculatorTest {
	private static final double DELTA = 0.00001;

	@Test
	public void testGetSentiment() throws Exception {
		AdjectiveCalculator calculator = new AdjectiveCalculator();
		assertEquals(-0.298, calculator.getSentiment("I am not happy at the moment."), DELTA);
		assertEquals(0.085, calculator.getSentiment("The cost of the car was way too high."), DELTA);
		assertEquals(0.097, calculator.getSentiment("I sometimes pity people living in large cities."), DELTA);
		assertEquals(5.000000000000004E-4, calculator.getSentiment("Three ugly witches made a magic potion."), DELTA);
		assertEquals(0.0445, calculator.getSentiment("Hand me the yellow plastic bowl."), DELTA);
		assertEquals(0.835, calculator.getSentiment("Linda’s hair is gorgeous."), DELTA);
		assertEquals(0, calculator.getSentiment("This shop is much nicer."), DELTA);
		assertEquals(0.194, calculator.getSentiment("Don’t be afraid of the dark."), DELTA);
		assertEquals(-0.032, calculator.getSentiment("The doctor is very late."), DELTA);

	}

}
