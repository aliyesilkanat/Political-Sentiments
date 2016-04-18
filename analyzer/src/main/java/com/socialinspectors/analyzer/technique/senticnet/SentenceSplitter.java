package com.socialinspectors.analyzer.technique.senticnet;

import java.util.List;

import org.apache.xalan.xsltc.compiler.sym;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;

import edu.stanford.nlp.ling.Label;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TrueCaseAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TrueCaseTextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.TrueCaseAnnotator;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class SentenceSplitter {
	private String[] conjunctions = new String[] { "nevertheless", "on the other hand", "but", "yet", "despite",
			"in contrast", "however", "though" };
	private String COMMA = ",";

	public String split(CoreMap sentenceLabel) {
		// List<CoreLabel> list = sentenceLabel.get(TokensAnnotation.class);
		// for (int i = 1; i < list.size(); i++) {
		// CoreLabel coreLabel = list.get(i);
		// System.err.println(coreLabel.originalText());
		// for (String conj : conjunctions) {
		// if (coreLabel.originalText().toLowerCase().contains(conj)) {
		// System.err.println(conj);
		// }
		//
		// }
		// }
		Tree tree2 = sentenceLabel.get(TreeAnnotation.class);
		System.out.println(tree2.pennString());
		Tree tree = tree2.getChild(0); //
		// sentence
		// clause
		// repeat over each clause
		String clauseSentence = "";
		boolean found = false;
		for (String conj : conjunctions) {
			if (tree.children()[0].toString().toLowerCase().contains(createWord(conj))) {
				for (int i = 1; i < tree.children().length; i++) {
					if (tree.children()[i].label().value().contains(COMMA)) {
						for (int j = i + 1; j < tree.children().length; j++) {
							for (Label yield : tree.children()[j].yield()) {
								clauseSentence += " " + yield.value();
							}
						}
						found = true;
					}
					if (found) {
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		if (!found) {
			for (String conj : conjunctions) {
				for (int i = 1; i < tree.children().length; i++) {
					String lowerCase = tree.children()[i].toString().toLowerCase();
					if ((lowerCase.contains(createWord(conj)) || lowerCase.contains(COMMA + conj))
							|| lowerCase.contains(conj + COMMA)) {

						for (int j = i; j < tree.children().length; j++) {
							for (Label yield : tree.children()[j].yield()) {
								clauseSentence += " " + yield.value();
							}
						}
						found = true;
					}
					if (found) {
						break;
					}
				}
				if (found) {
					break;
				}
			}
		}
		return createWord(clauseSentence);
		// return "";
	}

	private String createWord(String conj) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" ");
		stringBuilder.append(conj);
		stringBuilder.append(" ");
		return stringBuilder.toString();
	}
}
