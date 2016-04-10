package com.socialinspectors.analyzer.calculations.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class SentiCsv {
	String oldMax= "64.28214107053527";
	String oldIndex="0.009689";
	public static void main(String[] args) {

		try {
			new SentiCsv().run("movie_results_total.csv");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void run(String url) {
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
				if (Double.parseDouble(split[1]) > 0.009689)
					temp.add(new Comment(split[0], "Positive", split[2]));
				else
					temp.add(new Comment(split[0], "Negative", split[2]));
			}

			int cnt = 0;
			for (Comment comment : temp) {
				if (!comment.getResult().equals(comment.getScore())) {
					cnt++;
					System.out.println("real:" + comment.getResult() + "," + "actual:" + comment.getScore() + "---"
							+ comment.getContent());
				}
			}

			System.out.println("Overall Performance % :" + (temp.size() - cnt) * 100 / (temp.size() * 1.0));

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
	}
}
