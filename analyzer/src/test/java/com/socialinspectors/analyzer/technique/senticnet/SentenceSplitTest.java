package com.socialinspectors.analyzer.technique.senticnet;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentenceSplitTest {
	@Test
	public void splitTest() throws Exception {
		String but = "Buy a different phone - but not this.";
		String[] sentences = new String[] { "I love my school however, it is too easy for me.",
				"Though I’m flexible, I draw the line about that", "I don't know it however, It was not hard much.",
				"The festival was to be held today; however, it was canceled due to the rainy weather.",
				"However I do not know about it, it was easy." };
		for (String sentence : sentences) {
			List<CoreMap> coreSentences = CoreNlpPipeline.getPipeline().process("Sure, it is a fun phone, but try getting some signal!")
					.get(CoreAnnotations.SentencesAnnotation.class);
			System.err.println(new SentenceSplitter().split(coreSentences.get(0)));
		}
	}
}
