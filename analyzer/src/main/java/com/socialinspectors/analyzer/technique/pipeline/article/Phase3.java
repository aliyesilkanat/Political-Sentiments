package com.socialinspectors.analyzer.technique.pipeline.article;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveAndVerbCalculator;
import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;
import com.socialinspectors.analyzer.technique.senticnet.DependencyCalculator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Phase3 extends Phase {

	private static final String PHASE3_TRAINING_DATA_CSV = "phase3_training_data.csv";

	public static void main(String[] args) {
		try {
			new Phase3().execute();
		} catch (IOException e) {
			getLogger().error(e);
		}
	}

	@Override
	protected void execute() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(Phase.LABELLED_SENTENCES_CLEAN_TXT)));
		writer = new BufferedWriter(new FileWriter(getCleanDataset(PHASE3_TRAINING_DATA_CSV)));
		String strLine;
		while ((strLine = br.readLine()) != null) {

			strLine = strLine.replace("\n", "");
			String s = strLine.charAt(strLine.length() - 1) + "";
			if (s.equals("1") || s.equals("0")) {
				String sentence = strLine.substring(0, strLine.length() - 1);
				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(sentence)
						.get(CoreAnnotations.SentencesAnnotation.class);
				CoreMap coreMap = sentences.get(0);
				SemanticGraph semanticGraph = coreMap.get(CollapsedDependenciesAnnotation.class);
				double adjPolarity = new DependencyCalculator().traverseAdjectives(coreMap);
				double verbPolarity = new AdjectiveAndVerbCalculator().getVerbPolarity(semanticGraph);
				AnalysisResultHolder holder = new AnalysisResultHolder(adjPolarity, verbPolarity);
				if (AdjectiveAndVerbCalculator.getLogger().isInfoEnabled()) {
					AdjectiveAndVerbCalculator.getLogger().info(
							"calculated adj score: {}, verb score: {}, sentence: {}", adjPolarity, verbPolarity,
							coreMap.toString());
				}
				if (verbPolarity != 0 || adjPolarity != 0) {
					write(writer, holder.getAdjPolarity(), holder.getVerbPolarity(), s);
				}

			}
		}
		writer.close();
	}

}
