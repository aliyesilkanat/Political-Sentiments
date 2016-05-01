package com.socialinspectors.analyzer.technique.senticnet;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class SentenceSplitter {
	private static final String[] VERBS = { "VB", "VBD", "VBG", "VBN", "VBP", "VBZ " };
	private final Logger logger = LogManager.getLogger(SentenceSplitter.class);
	private String[] conjunctions = new String[] { "nevertheless", "on the other hand", "but", "yet", "despite",
			"in contrast", "however", "though" };

	public String split(CoreMap sentenceLabel) {
		String originalText = sentenceLabel.get(TextAnnotation.class);
		String splitSentence = "";
		splitSentence = splitSentence.concat(originalText);
		boolean found = false;
		for (String conj : conjunctions) {
			String substring = splitSentence.substring(splitSentence.indexOf(" "), splitSentence.length());
			int indexOf = substring.toLowerCase(Locale.ENGLISH).indexOf(conj);
			if (indexOf != -1) {
				splitSentence = substring.substring(indexOf, substring.length());
				SemanticGraph semanticGraph = CoreNlpPipeline.getPipeline().process(splitSentence)
						.get(CoreAnnotations.SentencesAnnotation.class).get(0)
						.get(CollapsedDependenciesAnnotation.class);
				List<IndexedWord> allNodesByPartOfSpeechPattern = semanticGraph.getAllNodesByPartOfSpeechPattern("JJ");
				if (allNodesByPartOfSpeechPattern.size() > 0) {
					if (getLogger().isTraceEnabled()) {
						getLogger().trace("split sentence, original sentence: {}, split: {}, conjunction: {}",
								originalText, splitSentence, conj);
					}
					found = true;
					break;
				} else {
					for (String verb : VERBS) {
						if (semanticGraph.getAllNodesByPartOfSpeechPattern(verb).size() > 0) {
							if (getLogger().isTraceEnabled()) {
								getLogger().trace("split sentence, original sentence: {}, split: {}, conjunction: {}",
										originalText, splitSentence, conj);
							}
							found = true;
							break;
						}
						if (found) {
							break;
						}
					}
				}

			}
		}
		if (found) {
			return splitSentence;

		} else {
			return originalText;
		}
	}

	public Logger getLogger() {
		return logger;
	}
}
