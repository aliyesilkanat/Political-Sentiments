package com.socialinspectors.analyzer.technique.pipeline.article;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LabelledSentenceDatasetCreator extends Phase{

	@Override
	protected void execute() throws IOException {
		BufferedReader br = new BufferedReader(
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream(LABELLED_SENTENCES_TXT)));
		writer = new BufferedWriter(new FileWriter("labelled_sentence_clear_dataset.txt"));
		String strLine;
		while ((strLine = br.readLine()) != null) {

			strLine = strLine.replace("\n", "");
			String s = strLine.charAt(strLine.length() - 1) + "";
			if (s.equals("1") || s.equals("0")) {
				writer.append(strLine+"\n");
				writer.flush();
			}
		}
		writer.close();		
	}public static void main(String[] args) throws IOException {
		new LabelledSentenceDatasetCreator().execute();
	}
	

}
