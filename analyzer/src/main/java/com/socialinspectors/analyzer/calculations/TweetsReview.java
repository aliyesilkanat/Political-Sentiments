package com.socialinspectors.analyzer.calculations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.socialinspectors.analyzer.technique.senticnet.DependencyCalculator;

public class TweetsReview {
	private static final String DELIMATER = "///////";
	private BufferedWriter writer;

	public static void main(String[] args) {
		try {
			new TweetsReview().run();
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
				new InputStreamReader(getClass().getClassLoader().getResourceAsStream("tweets.txt")));
		writer = new BufferedWriter(new FileWriter("tweet_results_total_after_word2vec.csv"));
		String strLine;
		DependencyCalculator calculator = new DependencyCalculator();
		while ((strLine = br.readLine()) != null) {
			String datasetSentiment = "Positive";
			if (strLine.charAt(0) == '0') {
				datasetSentiment = "Negative";
			}
			String tweet = strLine.substring(2);
			write(tweet, Double.toString(calculator.getSentiment(tweet)), datasetSentiment);
		}
		writer.close();
		br.close();
	}

	public synchronized void write(String a, String b, String c) {
		try {
			writer.append(a);
			writer.append(DELIMATER);
			writer.append(b);
			writer.append(DELIMATER);
			writer.append(c);
			writer.append(DELIMATER + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
