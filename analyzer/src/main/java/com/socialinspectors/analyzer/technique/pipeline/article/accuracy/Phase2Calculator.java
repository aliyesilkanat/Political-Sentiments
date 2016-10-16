package com.socialinspectors.analyzer.technique.pipeline.article.accuracy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.model.article.Phase1ModelTrainer;
import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveAndVerbCalculator;
import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;
import com.socialinspectors.analyzer.technique.senticnet.DependencyCalculator;
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

public class Phase2Calculator extends PhaseCalculator{
	private static final String PHASE = "phase2";

	public Phase2Calculator() throws IOException, InterruptedException {
		super(PHASE, new Phase1ModelTrainer(PHASE));
	}

	public static void main(String[] args) {
		try {
			new Phase2Calculator().calculate();
		} catch (IOException e) {
			getLogger().error(e);
		} catch (InterruptedException e) {
			getLogger().error(e);
		}
	}

	private BufferedWriter writer;

	@Override
	public void calculate() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(LABELLED_SENTENCES_CLEAN_TXT)));
		writer = new BufferedWriter(new FileWriter(getResultCleanedDataset()));
		String strLine;
		while ((strLine = br.readLine()) != null) {

			strLine = strLine.replace("\n", "");
			String s = strLine.charAt(strLine.length() - 1) + "";
			if (s.equals("1") || s.equals("0")) {
				String sentence = strLine.substring(0, strLine.length() - 1);
				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(sentence)
						.get(CoreAnnotations.SentencesAnnotation.class);
				CoreMap coreMap = sentences.get(0);
				AdjectiveAndVerbCalculator r = new AdjectiveAndVerbCalculator();
				SemanticGraph semanticGraph = coreMap.get(CollapsedDependenciesAnnotation.class);

				double adjPolarity = getAdjPolarity(coreMap);
				double verbPolarity = getVerbPolarity(r, semanticGraph);
				if (AdjectiveAndVerbCalculator.getLogger().isInfoEnabled()) {
					AdjectiveAndVerbCalculator.getLogger().info(
							"calculated adj score: {}, verb score: {}, sentence: {}", adjPolarity, verbPolarity,
							coreMap.toString());
				}
				AnalysisResultHolder result = new AnalysisResultHolder(adjPolarity, verbPolarity);
				double resultSentiment = getResultSentiment(result);
				if (s.equals("1")) {
					s = "Positive";
				} else {
					s = "Negative";
				}
				write(sentence, Double.toString(resultSentiment), s, writer);
			}
		}
		writer.close();
	}
	private double getAdjPolarity(CoreMap coreMap) {
		SemanticGraph semanticGraph = coreMap.get(CollapsedDependenciesAnnotation.class);
		List<MeaningfulObject> meaningfulObjectList = new ArrayList<MeaningfulObject>();
		// traversing each adjective
		for (IndexedWord adjectiveWord : semanticGraph
				.getAllNodesByPartOfSpeechPattern(DependencyCalculator.ADJECTIVE)) {
			// looking forward to find adjective is modifying to what?
			IndexedWord modifiedObject = null;

			// found negation modifier, this can be found only in out edges
			// of an adjective.
			boolean foundNeg = false;

			// traversing out edges of an adjective.
			for (SemanticGraphEdge semanticGraphEdge : semanticGraph.getOutEdgesSorted(adjectiveWord)) {
				GrammaticalRelation relation = semanticGraphEdge.getRelation();
				if (relation.toString().equals(DependencyCalculator.COPULA)) {
					IndexedWord target = semanticGraphEdge.getTarget();
					if (DependencyCalculator.getLogger().isTraceEnabled()) {
						DependencyCalculator.getLogger().trace("found copula dependency modifying: {}, adjective {}",
								target.toString(), adjectiveWord.toString());
					}
				} else if (relation.toString().equals(DependencyCalculator.NEGATION_MODIFIER)) {
					if (DependencyCalculator.getLogger().isTraceEnabled()) {
						DependencyCalculator.getLogger().trace("found negation modifying to adjective: {}",
								adjectiveWord.toString());
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
				if (semanticGraphEdge.getRelation().toString().equals(DependencyCalculator.ADJECTIVAL_MODIFIER)) {

					modifiedObject = semanticGraphEdge.getSource();
					if (DependencyCalculator.getLogger().isTraceEnabled()) {
						DependencyCalculator.getLogger().trace("found amod dependency modifying: {}, adjective {}",
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
			if (meaningfulObject instanceof Adjective) {
				Adjective adj = (Adjective) meaningfulObject;
				String lemmaAdj = adj.getLemmaFromWord(adj.getAdjective().originalText());
				double polarity = SenticNetModel.getInstance().getPolarity(lemmaAdj);
				if (adj.isNegationModifier()) {
					polarity = polarity * -1;
				}
				mean += polarity;
			} else {
				TargetObject targetObj = (TargetObject) meaningfulObject;
				double totalSum = 0;
				for (Adjective adjective : targetObj.getAdjectiveList()) {
					String lemmaAdj = adjective.getLemmaFromWord(adjective.getAdjective().originalText());
					double polarity = SenticNetModel.getInstance().getPolarity(lemmaAdj);
					if (adjective.isNegationModifier()) {
						polarity = polarity * -1;
					}
					totalSum += polarity;
				}
				if (totalSum != 0) {
					totalSum = totalSum / targetObj.getAdjectiveList().size();
				}
				mean += totalSum;
			}
		}
		if (mean != 0) {
			mean = mean / meaningfulObjectList.size();
		}
		double adjPolarity = mean;
		return adjPolarity;
	}
	private double getVerbPolarity(AdjectiveAndVerbCalculator r, SemanticGraph semanticGraph) {
		double verbPolarity = 0;
		boolean foundNeg = false;
		for (String verbConst : AdjectiveAndVerbCalculator.VERBS) {
			List<IndexedWord> verbsPos = semanticGraph.getAllNodesByPartOfSpeechPattern(verbConst);

			for (IndexedWord verb : verbsPos) {
				String lemmadVerb = verb.lemma();
				if (Arrays.asList(AdjectiveAndVerbCalculator.SKIPPED_VERBS).contains(lemmadVerb)) {
					continue;
				}

				for (SemanticGraphEdge graphEdge : semanticGraph.getOutEdgesSorted(verb)) {
					if (graphEdge.getRelation().toString().equals(AdjectiveAndVerbCalculator.NEGATION_MODIFIER)) {
						if (AdjectiveAndVerbCalculator.getLogger().isTraceEnabled()) {
							AdjectiveAndVerbCalculator.getLogger().trace("found negation modifier on verb: {}", verb);
						}
						foundNeg = true;
					}
				}
				verbPolarity = SenticNetModel.getInstance().getPolarity(lemmadVerb);
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

}
