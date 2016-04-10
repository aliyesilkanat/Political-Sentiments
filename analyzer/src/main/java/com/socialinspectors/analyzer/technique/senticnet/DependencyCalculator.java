package com.socialinspectors.analyzer.technique.senticnet;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.postag.Adjective;
import com.socialinspectors.analyzer.technique.senticnet.postag.MeaningfulObject;
import com.socialinspectors.analyzer.technique.senticnet.postag.TargetObject;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.util.CoreMap;

public class DependencyCalculator extends SenticNetCalculator {
	private static final String ADJECTIVE = "JJ";
	private static final String ADJECTIVAL_MODIFIER = "amod";
	private static final String NEGATION_MODIFIER = "neg";
	private static final String COPULA = "cop";

	@Override
	public double getSentiment(String tweet) throws Exception {

		List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(tweet)
				.get(CoreAnnotations.SentencesAnnotation.class);
		double score = traverseSentences(sentences);
		if (getLogger().isInfoEnabled()) {
			getLogger().info("calculated score: {}, tweet: {}", score, tweet);
		}
		return score;
	}

	public double traverseSentences(List<CoreMap> sentences) throws Exception {
		double score = 0;
		ConcurrentLinkedQueue<Double> sentencesPolarity = new ConcurrentLinkedQueue<Double>();
		// extract sentences
		for (CoreMap sentence : sentences) {
			// traverse adjectives
			sentencesPolarity.add(traverseAdjectives(sentence));

		}
		if (sentencesPolarity.size() != 0) {
			score = sentencesPolarity.stream().mapToDouble(val -> val).average().getAsDouble();
		}
		return score;

	}

	public double traverseAdjectives(CoreMap sentence) {
		SemanticGraph semanticGraph = sentence.get(CollapsedDependenciesAnnotation.class);
		List<MeaningfulObject> meaningfulObjectList = new ArrayList<MeaningfulObject>();
		// traversing each adjective
		for (IndexedWord adjectiveWord : semanticGraph.getAllNodesByPartOfSpeechPattern(ADJECTIVE)) {
			// looking forward to find adjective is modifying to what?
			IndexedWord modifiedObject = null;

			// found negation modifier, this can be found only in out edges
			// of an adjective.
			boolean foundNeg = false;

			// traversing out edges of an adjective.
			for (SemanticGraphEdge semanticGraphEdge : semanticGraph.getOutEdgesSorted(adjectiveWord)) {
				GrammaticalRelation relation = semanticGraphEdge.getRelation();
				if (relation.toString().equals(COPULA)) {
					IndexedWord target = semanticGraphEdge.getTarget();
					if (getLogger().isTraceEnabled()) {
						getLogger().trace("found copula dependency modifying: {}, adjective {}", target.toString(),
								adjectiveWord.toString());
					}
				} else if (relation.toString().equals(NEGATION_MODIFIER)) {
					if (getLogger().isTraceEnabled()) {
						getLogger().trace("found negation modifying to adjective: {}", adjectiveWord.toString());
					}
					foundNeg = true;
				}
				if (foundNeg && modifiedObject != null) {
					// found one copula and one negation modifier, no need
					// to iterate more.
					break;
				}
			}
			if (foundNeg && modifiedObject != null) {
				// found one copula and one negation modifier, there can not
				// be any amod dependency after now.
				break;
			}
			// traversing incoming edges trying to find amod, if there is no
			// copula.
			for (SemanticGraphEdge semanticGraphEdge : semanticGraph.getIncomingEdgesSorted(adjectiveWord)) {
				if (semanticGraphEdge.getRelation().toString().equals(ADJECTIVAL_MODIFIER)) {

					modifiedObject = semanticGraphEdge.getSource();
					if (getLogger().isTraceEnabled()) {
						getLogger().trace("found amod dependency modifying: {}, adjective {}",
								modifiedObject.toString(), adjectiveWord.toString());
					}
				}
				if (modifiedObject != null) {
					// found one amod, no need to iterate more.
					break;
				}
			}

			if (modifiedObject != null) {
				TargetObject foundSameModifiedObject = null;
				for (MeaningfulObject meaningfulObject : meaningfulObjectList) {
					if ((meaningfulObject instanceof TargetObject)
							&& ((TargetObject) meaningfulObject).getModifiedObject().compareTo(modifiedObject) == 0) {
						foundSameModifiedObject = (TargetObject) meaningfulObject;
						break;
					}

				}

				if (foundSameModifiedObject == null) {
					meaningfulObjectList.add(new TargetObject(modifiedObject, adjectiveWord, foundNeg));
				} else {
					foundSameModifiedObject.addNewAdjective(adjectiveWord, foundNeg);
				}

			} else {
				meaningfulObjectList.add(new Adjective(foundNeg, adjectiveWord));
			}

		}
		double mean = 0;
		for (MeaningfulObject meaningfulObject : meaningfulObjectList) {
			mean += meaningfulObject.getSentiment();
		}
		if (mean != 0) {
			mean = mean / meaningfulObjectList.size();
		}
		return mean;
	}

}
