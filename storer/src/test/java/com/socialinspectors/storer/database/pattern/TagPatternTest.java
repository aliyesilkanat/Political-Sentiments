package com.socialinspectors.storer.database.pattern;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.socialinspectors.storer.database.pattern.tag.TagsPattern;

public class TagPatternTest {

	@Test
	public void testTagPattern() throws Exception {
		String json = new TagsPattern(1, "OpenNLPClassifier",
				"Apache OpenNLP used classifier algorithm").getDocument()
				.toJson();
		System.out.println(json);
		assertEquals(
				"{ \"id\" : 1, \"name\" : \"OpenNLPClassifier\", \"description\" : \"Apache OpenNLP used classifier algorithm\" }",
				json);
	}
}
