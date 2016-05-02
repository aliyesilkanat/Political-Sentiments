package com.socialinspectors.analyzer.model.training;

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

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.pipeline.SentenceAnalysisPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class ParagraphModelTrainer {
	private static final CharSequence DELIMATER = ",";
	private ExecutorService threadPool = Executors.newFixedThreadPool(20);

	public static void main(String[] args) {
		try {
			new ParagraphModelTrainer().train();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void train() throws IOException {
		final SentenceAnalysisPipeline analysisPipeline = new SentenceAnalysisPipeline();
		BufferedWriter writer = new BufferedWriter(new FileWriter("neg__paragraph_three_group_ann_input.csv"));
		// extractSentiment(writer,
		// getClass().getClassLoader().getResource("movie_reviews/pos"), 1,
		// analysisPipeline);
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/neg"), 0, analysisPipeline);
		writer.flush();
	}

	private void extractSentiment(final BufferedWriter writer, URL resource, final int datasetScore,
			SentenceAnalysisPipeline analysisPipeline) throws IOException {

		File[] files = new File(resource.getPath()).listFiles();
		for (File file : files) {
			{
				final String comment = FileUtils.readFileToString(file).replaceAll("\n", "");
				List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(comment)
						.get(CoreAnnotations.SentencesAnnotation.class);
				threadPool.execute(new Runnable() {

					@Override
					public void run() {
						try {

							if (sentences.size() == 1) {
								double extractSentiment = getSentimentOfSentence(analysisPipeline, sentences);
								write(writer, datasetScore, 0.0, 0.0, extractSentiment);
							} else if (sentences.size() == 2) {
								double score1 = getSentimentOfSentence(analysisPipeline, sentences);
								double score2 = getSentimentOfSentence(analysisPipeline, sentences);
								write(writer, datasetScore, 0.0, score1, score2);
							} else if (sentences.size() > 2) {
								ConcurrentLinkedQueue<Double> firstGroup = new ConcurrentLinkedQueue<Double>();
								ConcurrentLinkedQueue<Double> secondGroup = new ConcurrentLinkedQueue<Double>();
								ConcurrentLinkedQueue<Double> thirdGroup = new ConcurrentLinkedQueue<Double>();
								int division = (int) sentences.size() / 3;

								for (int i = 0; i < division; i++) {
									addSentimentScoreToList(analysisPipeline, sentences, firstGroup, i);
								}
								for (int i = division; i < division * 2; i++) {
									addSentimentScoreToList(analysisPipeline, sentences, secondGroup, i);
								}
								for (int i = division * 2; i < sentences.size(); i++) {
									addSentimentScoreToList(analysisPipeline, sentences, thirdGroup, i);
								}
								nullCheck(firstGroup);
								nullCheck(secondGroup);
								nullCheck(thirdGroup);
								write(writer, datasetScore,
										firstGroup.stream().mapToDouble(val -> val).average().getAsDouble(),
										secondGroup.stream().mapToDouble(val -> val).average().getAsDouble(),
										thirdGroup.stream().mapToDouble(val -> val).average().getAsDouble());
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					private double getSentimentOfSentence(SentenceAnalysisPipeline analysisPipeline,
							List<CoreMap> sentences) {
						double score = 0.0;
						try {
							score = analysisPipeline.extractSentiment(sentences.get(0));
						} catch (Exception e) {
							e.printStackTrace();
						}
						return score;
					}

					private void nullCheck(ConcurrentLinkedQueue<Double> firstGroup) {
						if (firstGroup.size() == 0) {
							firstGroup.add(0.0);
						}
					}

					private void addSentimentScoreToList(SentenceAnalysisPipeline analysisPipeline,
							List<CoreMap> sentences, ConcurrentLinkedQueue<Double> firstGroup, int i) {
						try {
							firstGroup.add(analysisPipeline.extractSentiment(sentences.get(i)));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});

			}

		}

	}

	private synchronized void write(final BufferedWriter writer, int datasetSentiment, double score1, double score2,
			double score3) throws IOException {
		writer.append(datasetSentiment + "");
		writer.append(DELIMATER);
		writer.append(Double.toString(score1));
		writer.append(DELIMATER);
		writer.append(Double.toString(score2));
		writer.append(DELIMATER);
		writer.append(Double.toString(score3));
		writer.append("\n");
		writer.flush();
	}
}
