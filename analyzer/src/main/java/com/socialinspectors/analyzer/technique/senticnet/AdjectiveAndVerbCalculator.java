package com.socialinspectors.analyzer.technique.senticnet;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.model.Word2VecModel;
import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class AdjectiveAndVerbCalculator {
	public static final String NEGATION_MODIFIER = "neg";
	private static final Logger logger = LogManager.getLogger(AdjectiveAndVerbCalculator.class);
	public static final String[] SKIPPED_VERBS = { "am", "is", "are", "do", "have", "was", "were", "be" };
	public static final String[] VERBS = { "VB", "VBD", "VBG", "VBN", "VBP", "VBZ " };

	public static Logger getLogger() {
		return logger;
	}

	public double extractOneSentenceAdjectiveSentiment(SemanticGraph semanticGraph) {

		// get adjectives list
		List<IndexedWord> adjectiveList = semanticGraph.getAllNodesByPartOfSpeechPattern("JJ");
		ConcurrentLinkedQueue<Double> adjectivesPolarity = new ConcurrentLinkedQueue<Double>();
		traversAdjectives(semanticGraph, adjectiveList, adjectivesPolarity);
		return adjectivesPolarity.stream().mapToDouble(val -> val).average().getAsDouble();
	}

	/**
	 * Gets lemma from given word
	 * 
	 * @param adjective
	 *            word
	 * @return first lemma of the word
	 */
	public String getLemmaFromWord(String adjective) {
		Annotation annotation = new Annotation(adjective);
		CoreNlpPipeline.getPipeline().annotate(annotation);
		String lemma = annotation.get(SentencesAnnotation.class).get(0).get(TokensAnnotation.class).get(0)
				.get(LemmaAnnotation.class);
		return lemma;
	}

	public AnalysisResultHolder getSentiment(CoreMap sentence)  {
		SemanticGraph semanticGraph = sentence.get(CollapsedDependenciesAnnotation.class);
		double adjPolarity = extractOneSentenceAdjectiveSentiment(semanticGraph);
		double verbPolarity = getVerbPolarity(semanticGraph);
		AnalysisResultHolder holder = new AnalysisResultHolder(adjPolarity, verbPolarity);
		if (getLogger().isInfoEnabled()) {
			getLogger().info("calculated adj score: {}, verb score: {}, sentence: {}", adjPolarity, verbPolarity,
					sentence.toString());
		}
		return holder;
	}

	public double getVerbPolarity(SemanticGraph semanticGraph) {
		double verbPolarity = 0;
		boolean foundNeg = false;
		for (String verbConst : VERBS) {
			List<IndexedWord> verbsPos = semanticGraph.getAllNodesByPartOfSpeechPattern(verbConst);

			for (IndexedWord verb : verbsPos) {
				String lemmadVerb = verb.lemma();
				if (Arrays.asList(SKIPPED_VERBS).contains(lemmadVerb)) {
					continue;
				}

				for (SemanticGraphEdge graphEdge : semanticGraph.getOutEdgesSorted(verb)) {
					if (graphEdge.getRelation().toString().equals(NEGATION_MODIFIER)) {
						if (getLogger().isTraceEnabled()) {
							getLogger().trace("found negation modifier on verb: {}", verb);
						}
						foundNeg = true;

					}
				}
				verbPolarity = SenticNetModel.getInstance().getPolarity(lemmadVerb);
				if (verbPolarity == 0) {
					for (String nearestVerb : Word2VecModel.getInstance().findNearest(lemmadVerb)) {
						verbPolarity = SenticNetModel.getInstance().getPolarity(nearestVerb);
						if (verbPolarity != 0) {

							break;
						}
					}
				}
				if (verbPolarity != 0) {
					break;

				}
			}
			if (verbPolarity != 0) {
				break;

			}
		}
		if (foundNeg) {
			verbPolarity = verbPolarity * -1;
		}
		return verbPolarity;
	}

	public void traversAdjectives(SemanticGraph semanticGraph, List<IndexedWord> adjectiveList,
			ConcurrentLinkedQueue<Double> adjectivesPolarity)  {
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

	public double traverseDependentEdges(double polarity, List<SemanticGraphEdge> posList, IndexedWord adjective) {
		for (SemanticGraphEdge semanticGraphEdge : posList) {
			if (semanticGraphEdge.getRelation().toString().equals(NEGATION_MODIFIER)) {
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
}
