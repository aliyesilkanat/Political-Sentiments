package com.socialinspectors.analyzer.technique.pipeline.article.accuracy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.model.article.Phase1ModelTrainer;
import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveAndVerbCalculator;
import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

public class Phase1Calculator extends PhaseCalculator {
	private static final String PHASE = "phase1";

	public Phase1Calculator() throws IOException, InterruptedException {
		super(PHASE, new Phase1ModelTrainer(PHASE));
	}

	public static void main(String[] args) {
		try {
			new Phase1Calculator().calculate();
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
				double adjPolarity = extractAdjPolarity(r, semanticGraph);
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

	private double extractAdjPolarity(AdjectiveAndVerbCalculator r, SemanticGraph semanticGraph) {
		// get adjectives list
		List<IndexedWord> adjectiveList = semanticGraph.getAllNodesByPartOfSpeechPattern("JJ");
		ConcurrentLinkedQueue<Double> adjectivesPolarity = new ConcurrentLinkedQueue<Double>();
		double polarity = 0;
		for (IndexedWord adjective : adjectiveList) {
			String lemma = r.getLemmaFromWord(adjective.originalText());
			if (AdjectiveAndVerbCalculator.getLogger().isDebugEnabled()) {
				AdjectiveAndVerbCalculator.getLogger().debug("found adjective: {}, lemma: {}", adjective.originalText(),
						lemma);
			}
			// traverse adjectives
			polarity = 0;
			polarity = SenticNetModel.getInstance().getPolarity(lemma);
			List<SemanticGraphEdge> posList = semanticGraph.getOutEdgesSorted(adjective);
			polarity = r.traverseDependentEdges(polarity, posList, adjective);
			if (polarity != 0) {
				adjectivesPolarity.add(polarity);
			}
		}
		if (adjectivesPolarity.isEmpty()) {
			// If no adjective were found in the sentence, then give score as
			// 0.
			return 0.0;
		} else {
			return adjectivesPolarity.stream().mapToDouble(val -> val).average().getAsDouble();
		}
	}
}
