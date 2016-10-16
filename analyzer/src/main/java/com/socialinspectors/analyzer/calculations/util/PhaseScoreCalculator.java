package com.socialinspectors.analyzer.calculations.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class PhaseScoreCalculator {
	public static void main(String[] args) {
		for (int i = 1; i < 5; i++) {
			runPhase(i);
		}

	}

	private static void runPhase(int i3) {
		List<Double> scores = new ArrayList<Double>();
		List<Double> index = new ArrayList<Double>();
		int min = -1000;
		try {
			for (double i = min; i < 1000; i++) {
				double i2 = i / 10000;
				scores.add(new PhaseScoreCalculator().run("Result_clean_phase" + i3 + "_testresults.csv", i2));
				index.add(i2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		double max = Double.MIN_VALUE;
		double maxIndex = 0;
		for (int i = 0; i < scores.size(); i++) {
			if (scores.get(i) > max) {
				max = scores.get(i);
				maxIndex = index.get(i);
			}
		}
		System.out.println("Phase " + i3 + "Max score, " + max + " max index" + maxIndex);
	}

	private double run(String url, double i) {
		double score = 0;
		BufferedReader br = null;
		ArrayList<Comment> temp = new ArrayList<Comment>();
		try {
			FileInputStream fr = new FileInputStream(url);
			InputStreamReader isr = new InputStreamReader(fr, Charset.forName("UTF-8"));
			String sCurrentLine;
			br = new BufferedReader(isr);
			br.readLine();
			while ((sCurrentLine = br.readLine()) != null) {
				String[] split = sCurrentLine.split("///////");
				if (Double.parseDouble(split[1]) > i)
					temp.add(new Comment(split[0], "Positive", split[2]));
				else
					temp.add(new Comment(split[0], "Negative", split[2]));
			}

			int cnt = 0;
			for (Comment comment : temp) {
				if (!comment.getResult().equals(comment.getScore())) {
					cnt++;
					// System.out.println("real:" + comment.getResult() + "," +
					// "actual:" + comment.getScore() + "---"
					// + comment.getContent());
				}
			}

			score = (temp.size() - cnt) * 100 / (temp.size() * 1.0);
			// System.out.println("Overall Performance % :" + score + "
			// divident: " + i);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return score;
	}
}
