package com.socialinspectors.analyzer.calculations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.SentenceSplitter;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class SentenceSplitReview {
	private static final String DELIMATER = "///////";
	private BufferedWriter writer;
	private String[] conjunctions = new String[] { "nevertheless", "on the other hand", "but", "yet", "despite",
			"in contrast", "however", "though" };

	public static void main(String[] args) {
		try {
			new SentenceSplitReview().run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("labelled_sentences.txt")));
		writer = new BufferedWriter(new FileWriter("splitted_words.txt"));
		String strLine;

		SentenceSplitter split = new SentenceSplitter();
		while ((strLine = br.readLine()) != null) {
			for (String conj : conjunctions) {

				if (strLine.toLowerCase().contains(" "+conj+ " ")) {
					strLine = strLine.replace("\n", "");
					List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(strLine)
							.get(CoreAnnotations.SentencesAnnotation.class);
					try {
						String splittedSentence = split.split(sentences.get(0));
						write(strLine, splittedSentence);
					} catch (Exception e) {
						write(strLine, "GOT EXCEPTION");
						e.printStackTrace();
					}
				}
			}

		}
		writer.close();
		br.close();
	}

	public synchronized void write(String a, String b) {
		try {
			writer.append(a);
			writer.append(DELIMATER);
			writer.append(b + "\n");
			writer.append(DELIMATER + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
