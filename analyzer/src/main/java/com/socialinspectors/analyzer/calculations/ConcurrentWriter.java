package com.socialinspectors.analyzer.calculations;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ConcurrentWriter {
	private static ConcurrentWriter instance = null;
	private BufferedWriter writer;
	private static final String DELIMATER = "///////";

	private ConcurrentWriter() {

		try {
			writer = new BufferedWriter(new FileWriter("movie_results_total_multi.csv"));
			write("Comments", "Score", "Dataset Sentiment");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void closeWriter() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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

	public static ConcurrentWriter getInstance() {
		if (instance == null) {
			instance = new ConcurrentWriter();
		}
		return instance;
	}

}
