package com.socialinspectors.analyzer.technique.senticnet;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.technique.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class AdjectiveCalculator extends SenticNetCalculator {
	private static final Logger logger = LogManager.getLogger(AdjectiveCalculator.class);

	public double getSentiment(String tweet) throws Exception {

		List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(tweet)
				.get(CoreAnnotations.SentencesAnnotation.class);
		double score = traverseSentences(sentences, new ConcurrentLinkedQueue<Double>());
		if (getLogger().isInfoEnabled()) {
			getLogger().info("calculated score: {}, tweet: {}", score, tweet);
		}
		return score;
	}

	public double traverseSentences(List<CoreMap> sentences, ConcurrentLinkedQueue<Double> sentencesPolarity)
			throws Exception {
		// extract sentences
		for (CoreMap sentence : sentences) {
			// traverse sentences
			SemanticGraph semanticGraph = sentence.get(CollapsedDependenciesAnnotation.class);
			// get adjectives list
			List<IndexedWord> adjectiveList = semanticGraph.getAllNodesByPartOfSpeechPattern("JJ");
			ConcurrentLinkedQueue<Double> adjectivesPolarity = new ConcurrentLinkedQueue<Double>();
			traversAdjectives(semanticGraph, adjectiveList, adjectivesPolarity);
			sentencesPolarity.add(adjectivesPolarity.stream().mapToDouble(val -> val).average().getAsDouble());

		}
		return sentencesPolarity.stream().mapToDouble(val -> val).average().getAsDouble();
	}

	public void traversAdjectives(SemanticGraph semanticGraph, List<IndexedWord> adjectiveList,
			ConcurrentLinkedQueue<Double> adjectivesPolarity) throws Exception {
		double polarity = 0;
		for (IndexedWord adjective : adjectiveList) {
			String lemma = getLemmaFromWord(adjective.originalText());
			if (getLogger().isDebugEnabled()) {
				getLogger().debug("found adjective: {}, lemma: {}", adjective.originalText(), lemma);
			}
			// traverse adjectives
			polarity = 0;
			polarity = SenticNetModel.getInstance().getPolarity(lemma);
			List<SemanticGraphEdge> posList = semanticGraph.getOutEdgesSorted(adjective);
			polarity = traverseDependentEdges(polarity, posList, adjective);
			adjectivesPolarity.add(polarity);

		}
		if (adjectiveList.isEmpty()) {

			// If no adjective were found in the sentencen, then give score as
			// 0.
			adjectivesPolarity.add(polarity);
		}
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

	private double traverseDependentEdges(double polarity, List<SemanticGraphEdge> posList, IndexedWord adjective) {
		for (SemanticGraphEdge semanticGraphEdge : posList) {
			if (semanticGraphEdge.getRelation().equals("neg")) {
				if (getLogger().isDebugEnabled()) {
					// check if adjective is dependent to any negation
					// modifier
					getLogger().debug("found negation modifier, modifying adjective: {}", adjective);
				}
				polarity = polarity * -1;
			}
		}
		return polarity;
	}

	public static Logger getLogger() {
		return logger;
	}

}
