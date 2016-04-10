package com.socialinspectors.analyzer.model;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

public class Word2VecModelTest {
	@Test
	public void checkNearestWords() throws Exception {
		Collection<String> findNearest = Word2VecModel.getInstance().findNearest("fast");
		System.out.println(findNearest.size());
		String[] array = findNearest.toArray(new String[findNearest.size()]);
		assertEquals("quick", array[0]);

	}
}