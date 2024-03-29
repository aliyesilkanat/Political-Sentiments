package com.socialinspectors.analyzer.calculations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.pipeline.SentenceAnalysisPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;

public class NormalizedAndSplittedLabelledReview {
	private BufferedWriter writer;
	private static final String DELIMATER = "///////";

	public static void main(String[] args) {
		try {
			new NormalizedAndSplittedLabelledReview().run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("labelled_sentences.txt")));
		writer = new BufferedWriter(new FileWriter("normalized_splitted_sentences_scores.csv"));
		String strLine;
		while ((strLine = br.readLine()) != null) {

			strLine = strLine.replace("\n", "");
			String s = strLine.charAt(strLine.length() - 1) + "";
			if (s.equals("1") || s.equals("0")) {

				String substring = strLine.substring(0, strLine.length() - 1);
				double extractSentiment = new SentenceAnalysisPipeline().extractSentiment(
						CoreNlpPipeline.getPipeline().process(substring)
								.get(CoreAnnotations.SentencesAnnotation.class).get(0));
				if (s.equals("1")) {
					s = "Positive";
				} else {
					s = "Negative";
				}
				write(substring, Double.toString(extractSentiment), s);
			}

		}
		writer.close();
		br.close();
	}

	public synchronized void write(String a, String object, String s) {
		try {
			writer.append(a + DELIMATER);
			writer.append(object + DELIMATER);
			writer.append(s + DELIMATER + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
