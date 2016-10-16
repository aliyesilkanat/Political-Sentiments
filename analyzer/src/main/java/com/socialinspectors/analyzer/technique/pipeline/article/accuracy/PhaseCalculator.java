package com.socialinspectors.analyzer.technique.pipeline.article.accuracy;

import java.io.IOException;
import java.io.Writer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;

import com.socialinspectors.analyzer.model.MLPOutputNormalizer;
import com.socialinspectors.analyzer.model.article.ModelTrainer;
import com.socialinspectors.analyzer.technique.pipeline.SentenceAnalysisPipeline;
import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;

public abstract class PhaseCalculator {
	private MultiLayerNetwork network;
	private static final Logger logger = LogManager.getLogger(SentenceAnalysisPipeline.class);
	private static final CharSequence DELIMATER = "///////";
	private String phase;
	protected static final String LABELLED_SENTENCES_CLEAN_TXT = "labelled_sentence/testset.txt";

	public PhaseCalculator(String phaseName, ModelTrainer phase1ModelTrainer) throws IOException, InterruptedException {
		phase = phaseName;
		network = phase1ModelTrainer.train();

	}

	protected static Logger getLogger() {
		return logger;
	}

	protected String getResultCleanedDataset() {
		return "Result_clean_" + phase + "_testresults.csv";
	}

	abstract public void calculate() throws IOException;

	public synchronized void write(String a, String object, String s, Writer writer) {
		try {
			writer.append(a + DELIMATER);
			writer.append(object + DELIMATER);
			writer.append(s + DELIMATER + "\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected double getResultSentiment(AnalysisResultHolder holder) {
		double normalize = 0;
		if ((holder.getAdjPolarity() < 0) && (holder.getVerbPolarity() > 0)
				|| holder.getAdjPolarity() > 0 && holder.getVerbPolarity() < 0) {
			INDArray output = network.output(holder.getINDArray());
			normalize = new MLPOutputNormalizer().normalize(holder, output.getDouble(1) - 0.5);
		} else if (holder.getAdjPolarity() > 0 || holder.getVerbPolarity() > 0) {
			normalize = 1;
		}
		return normalize;

	}
}
