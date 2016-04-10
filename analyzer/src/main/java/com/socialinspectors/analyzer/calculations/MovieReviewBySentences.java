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

public class MovieReviewBySentences {
	final DependencyCalculator calculator = new DependencyCalculator();
	ExecutorService threadPool = Executors.newFixedThreadPool(20);
	private static final String DELIMATER = "///////";

	public static void main(String[] args) {
		try {
			new MovieReviewBySentences().run();
			System.out.println("Job done!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void run() throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("movie_results_by_sentences_after_dl11.csv"));
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/pos"), "Positive");
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/neg"), "Negative");
		writer.flush();
	}

	private void extractSentiment(final BufferedWriter writer, URL resource, final String string) throws IOException {

		File[] files = new File(resource.getPath()).listFiles();
		for (final File file : files) {
			{

				String comment = FileUtils.readFileToString(file);
				comment = comment.replaceAll("\n", "");
				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(comment)
						.get(CoreAnnotations.SentencesAnnotation.class);
				for (final CoreMap sentence : sentences) {
					threadPool.submit(new Runnable() {
						public void run() {
							double score = 0;
							try {
								score = calculator.traverseAdjectives(sentence);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								try {
									write(writer, string, sentence, score);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					});
				}

			}
		}
	}

	private synchronized void write(final BufferedWriter writer, String string, CoreMap sentence, double score)
			throws IOException {
		writer.append(sentence.toString());
		writer.append(DELIMATER);
		writer.append(Double.toString(score));
		writer.append(DELIMATER);
		writer.append(string);
		writer.append(DELIMATER + "\n");
		writer.flush();
	}
}
