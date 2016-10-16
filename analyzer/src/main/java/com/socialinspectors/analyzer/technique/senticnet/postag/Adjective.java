package com.socialinspectors.analyzer.technique.senticnet.postag;

import java.util.Iterator;
import java.util.List;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.model.Word2VecModel;
import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;

import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class Adjective extends MeaningfulObject {
	private boolean negationModifier = false;
	private IndexedWord adjective = null;

	public Adjective(boolean negationModifier, IndexedWord adjective) {
		this.negationModifier = negationModifier;
		this.adjective = adjective;
	}

	@Override
	public double getSentiment() {
		String lemmaAdj = getLemmaFromWord(adjective.originalText());
		double polarity = SenticNetModel.getInstance().getPolarity(lemmaAdj);
		Iterator<String> iterator = Word2VecModel.getInstance().findNearest(lemmaAdj).iterator();
		while (polarity == 0 && iterator.hasNext()) {
			lemmaAdj = getLemmaFromWord(iterator.next());
			polarity = SenticNetModel.getInstance().getPolarity(lemmaAdj);
		}
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
	public String getLemmaFromWord(String adjective) {
		String lemma = adjective;
		Annotation annotation = new Annotation(adjective);
		CoreNlpPipeline.getPipeline().annotate(annotation);
		List<CoreMap> list = annotation.get(SentencesAnnotation.class);

		if (list.size() > 0) {
			List<CoreLabel> lemmasList = list.get(0).get(TokensAnnotation.class);
			if (lemmasList.size() > 0) {
				lemma = lemmasList.get(0).get(LemmaAnnotation.class);
			}
		}
		return lemma;
	}

}