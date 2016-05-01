package com.socialinspectors.analyzer.calculations;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AdjectiveCalculator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class ConcurrentMoviewReview {
	final AdjectiveCalculator calculator = new AdjectiveCalculator();
	ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void main(String[] args) {
		try {
			new ConcurrentMoviewReview().run();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void run() throws IOException {
		try {
			extractSentiment(calculator, getClass().getClassLoader().getResource("movie_reviews/pos"), "Positive");
			extractSentiment(calculator, getClass().getClassLoader().getResource("movie_reviews/neg"), "Negative");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void extractSentiment(final AdjectiveCalculator calculator, URL resource, final String datasetSentiment)
			throws IOException {
		File[] files = new File(resource.getPath()).listFiles();
		for (final File file : files) {
			{
				pool.execute(new Runnable() {
					public void run() {
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
							double score1 = calculator.traverseSentences(sentences,
									new ConcurrentLinkedQueue<Double>());
							if (AdjectiveCalculator.getLogger().isInfoEnabled()) {
								AdjectiveCalculator.getLogger().info("calculated score: {}, tweet: {}", score1,
										comment);
							}
							score = score1;
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							ConcurrentWriter.getInstance().write(comment, Double.toString(score), datasetSentiment);
						}
					}
				});
			}

		}
	}
}
