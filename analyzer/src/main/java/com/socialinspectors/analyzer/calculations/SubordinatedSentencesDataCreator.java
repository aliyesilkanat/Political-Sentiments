package com.socialinspectors.analyzer.calculations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;

public class SubordinatedSentencesDataCreator {
	private BufferedWriter writer;
	private String[] conjunctions = new String[] { "nevertheless", "on the other hand", "but", "yet", "despite",
			"in contrast", "however", "though", "even if", "rather than", "unless" };

	public static void main(String[] args) {
		try {
			new SubordinatedSentencesDataCreator().run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void run() throws Exception {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("labelled_sentences.txt")));
		writer = new BufferedWriter(new FileWriter("subordinated_sentence.txt"));
		String strLine;

		while ((strLine = br.readLine()) != null) {
			for (String conj : conjunctions) {

				if (strLine.toLowerCase().contains(" " + conj + " ")) {
					strLine = strLine.replace("\n", "");
					write(strLine,
							CoreNlpPipeline.getPipeline().process(strLine)
									.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeAnnotation.class)
									.pennString());
				}
			}

		}
		writer.close();
		br.close();
	}

	public synchronized void write(String a, String object) {
		try {
			writer.append(a + "\n");
			writer.append(object + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
