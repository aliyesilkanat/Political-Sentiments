package com.socialinspectors.analyzer.technique.senticnet.postag;

import com.socialinspectors.analyzer.SenticNetModel;
import com.socialinspectors.analyzer.technique.CoreNlpPipeline;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;

public class Adjective extends MeaningfulObject {
	private boolean negationModifier = false;
	private IndexedWord adjective = null;

	public Adjective(boolean negationModifier, IndexedWord adjective) {
		this.negationModifier = negationModifier;
		this.adjective = adjective;
	}

	@Override
	public double getSentiment() {
		double polarity = SenticNetModel.getInstance().getPolarity(getLemmaFromWord(adjective.originalText()));
		if (negationModifier) {
			polarity = polarity * -1;
		}
		return polarity;
	}

	public boolean isNegationModifier() {
		return negationModifier;
	}

	public void setNegationModifier(boolean negationModifier) {
		this.negationModifier = negationModifier;
	}

	public IndexedWord getAdjective() {
		return adjective;
	}

	public void setAdjective(IndexedWord adjective) {
		this.adjective = adjective;
	}

	/**
	 * Gets lemma from given word
	 * 
	 * @param adjective
	 *            word
	 * @return first lemma of the word
	 */
	private String getLemmaFromWord(String adjective) {
		Annotation annotation = new Annotation(adjective);
		CoreNlpPipeline.getPipeline().annotate(annotation);
		String lemma = annotation.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class).get(0)
				.get(LemmaAnnotation.class);
		return lemma;
	}

}
