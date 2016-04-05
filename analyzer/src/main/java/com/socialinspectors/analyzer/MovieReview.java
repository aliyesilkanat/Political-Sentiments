package com.socialinspectors.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.SenticnetPolarityCalculator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class MovieReview {
	final SenticnetPolarityCalculator calculator = new SenticnetPolarityCalculator();
	private static final String DELIMATER = "///////";
	ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void main(String[] args) {
		try {
			new MovieReview().run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void run() throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("movie_results_total.csv"));
		writer.append("Comments");
		writer.append(DELIMATER);
		writer.append("Score");
		writer.append(DELIMATER);
		writer.append("Dataset sentiment");
		writer.append(DELIMATER + "\n");
		try {
			extractSentiment(calculator, writer, getClass().getClassLoader().getResource("movie_reviews/pos"),
					"Positive");
			extractSentiment(calculator, writer, getClass().getClassLoader().getResource("movie_reviews/neg"),
					"Negative");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extractSentiment(final SenticnetPolarityCalculator calculator, final BufferedWriter writer,
			URL resource, final String datasetSentiment) throws IOException {
		File[] files = new File(resource.getPath()).listFiles();
		for (final File file : files) {
			{
				double score = 0;
				String comment = null;
				try {
					comment = FileUtils.readFileToString(file);
					comment = comment.replaceAll("\n", "");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(comment)
							.get(CoreAnnotations.SentencesAnnotation.class);
					double score1 = calculator.traverseSentences(sentences, new ConcurrentLinkedQueue<Double>());
					if (SenticnetPolarityCalculator.getLogger().isInfoEnabled()) {
						SenticnetPolarityCalculator.getLogger().info("calculated score: {}, tweet: {}", score1, comment);
					}
					score = score1;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						writer.append(comment);
						writer.append(DELIMATER);
						writer.append(Double.toString(score));
						writer.append(DELIMATER);
						writer.append(datasetSentiment);
						writer.append(DELIMATER + "\n");
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

}
