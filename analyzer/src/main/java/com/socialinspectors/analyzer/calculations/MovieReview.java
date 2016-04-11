package com.socialinspectors.analyzer.calculations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.DependencyCalculator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class MovieReview {
	final DependencyCalculator calculator = new DependencyCalculator();
	private static final String DELIMATER = "///////";
	ExecutorService threadPool = Executors.newFixedThreadPool(30);

	public static void main(String[] args) {
		try {
			new MovieReview().run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void run() throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("movie_results_after_dl.csv"));
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/pos"), "Positive");
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/neg"), "Negative");
		writer.flush();
	}

	private void extractSentiment(final BufferedWriter writer, URL resource, final String string) throws IOException {

		File[] files = new File(resource.getPath()).listFiles();
		for (File file : files) {
			{
				final String comment = FileUtils.readFileToString(file).replaceAll("\n", "");

				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(comment)
						.get(CoreAnnotations.SentencesAnnotation.class);

				threadPool.submit(new Runnable() {
					public void run() {
						try {
							double score = calculator.traverseSentences(sentences);
							write(writer, string, comment, score);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

		}

	}

	private synchronized void write(final BufferedWriter writer, String datasetSentiment, String comment, double score)
			throws IOException {
		writer.append(comment);
		writer.append(DELIMATER);
		writer.append(Double.toString(score));
		writer.append(DELIMATER);
		writer.append(datasetSentiment);
		writer.append(DELIMATER + "\n");
		writer.flush();
	}

}