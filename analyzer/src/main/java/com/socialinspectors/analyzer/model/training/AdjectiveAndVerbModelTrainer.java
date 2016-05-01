package com.socialinspectors.analyzer.model.training;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.model.SenticNetModel;
import com.socialinspectors.analyzer.model.Word2VecModel;
import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveAndVerbCalculator;
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

public class AdjectiveAndVerbModelTrainer {
	private static final String ADJECTIVAL_MODIFIER = "amod";
	private static final String ADJECTIVE = "JJ";
	private static final String COPULA = "cop";
	private static final String DELIMATER = ",";
	private static final Logger logger = LogManager.getLogger(AdjectiveAndVerbCalculator.class);
	private static final String NEGATION_MODIFIER = "neg";
	private static final String[] VERBS = { "VB", "VBD", "VBG", "VBN", "VBP", "VBZ " };
	private ExecutorService threadPool = Executors.newFixedThreadPool(30);
	private BufferedWriter writer;

	public static Logger getLogger() {
		return logger;
	}

	public static void main(String[] args) {
		try {
			new AdjectiveAndVerbModelTrainer().train();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void executeCalculation(final String strLine, final String sentence, final List<CoreMap> sentences)
			throws Exception {
		threadPool.submit(new Runnable() {
			public void run() {
				int polarity = Integer.parseInt(strLine.charAt(strLine.length() - 1) + "");
				try {
					write(sentences, polarity, sentence);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private double getAdjPolarity(SemanticGraph semanticGraph) {
		List<MeaningfulObject> meaningfulObjectList = new ArrayList<MeaningfulObject>();
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
		double adjectiveMean = 0;
		for (MeaningfulObject meaningfulObject : meaningfulObjectList) {
			adjectiveMean += meaningfulObject.getSentiment();
		}
		if (adjectiveMean != 0) {
			adjectiveMean = adjectiveMean / meaningfulObjectList.size();
		}
		return adjectiveMean;
	}

	private double getVerbPolarity(SemanticGraph semanticGraph) {
		double verbPolarity = 0;
		for (String verb : VERBS) {
			List<IndexedWord> verbsPos = semanticGraph.getAllNodesByPartOfSpeechPattern(verb);
			if (verbsPos.size() > 0) {
				String lemmadVerb = verbsPos.get(0).lemma();
				verbPolarity = SenticNetModel.getInstance().getPolarity(lemmadVerb);
				Collection<String> findNearest = Word2VecModel.getInstance().findNearest(lemmadVerb);
				for (String nearestVerb : findNearest) {
					verbPolarity = SenticNetModel.getInstance().getPolarity(nearestVerb);
					if (verbPolarity != 0) {
						break;
					}
				}
				break;
			}
		}
		return verbPolarity;
	}

	private void train() throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("labelled_sentences.txt")));
		writer = new BufferedWriter(new FileWriter("labelled_sentences_adj_verb_training_data.csv"));
		String strLine;
		while ((strLine = br.readLine()) != null) {

			if (strLine.endsWith("1") || strLine.endsWith("0")) {
				String sentence = strLine.substring(0, strLine.length() - 2);
				// System.out.println(sentence + " " +
				// strLine.charAt(strLine.length() - 1));

				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(sentence)
						.get(CoreAnnotations.SentencesAnnotation.class);
				executeCalculation(strLine, sentence, sentences);
			}
		}
		// writer.close();
		br.close();
	}

	public synchronized void write(int polarity, double adjPolarity, double verbPolarity) {
		try {
			writer.append(polarity + "");
			writer.append(DELIMATER);
			writer.append(Double.toString(adjPolarity));
			writer.append(DELIMATER);
			writer.append(Double.toString(verbPolarity));
			writer.append("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(List<CoreMap> sentences, int polarity, String rawSentence) throws Exception {
		// extract sentences
		for (CoreMap sentence : sentences) {
			// traverse sentences
			SemanticGraph semanticGraph = sentence.get(CollapsedDependenciesAnnotation.class);

			// traversing each adjective
			double adjPolarity = getAdjPolarity(semanticGraph);
			double verbPolarity = getVerbPolarity(semanticGraph);
			if (verbPolarity != 0 || adjPolarity != 0) {
				write(polarity, adjPolarity, verbPolarity);
			}

		}

	}
}
