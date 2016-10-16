package com.socialinspectors.analyzer.technique.pipeline.article;

import java.io.BufferedWriter;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.socialinspectors.analyzer.technique.pipeline.SentenceAnalysisPipeline;

public abstract class Phase {
	private static final Logger logger = LogManager.getLogger(SentenceAnalysisPipeline.class);
	private static final CharSequence DELIMATER = ",";

	protected static Logger getLogger() {
		return logger;
	}

	protected BufferedWriter writer;
	protected static final String LABELLED_SENTENCES_TXT = "labelled_sentences.txt";
	protected static final String LABELLED_SENTENCES_CLEAN_TXT = "labelled_sentence/trainingset.txt";

	protected abstract void execute() throws IOException;

	protected synchronized void write(final BufferedWriter writer, double adjPol, double verbPol, String score)
			throws IOException {
		writer.append(score);
		writer.append(DELIMATER);
		writer.append(Double.toString(adjPol));
		writer.append(DELIMATER);
		writer.append(Double.toString(verbPol));
		writer.append("\n");
		writer.flush();
	}

	protected String getCleanDataset(String path) {
		return "Clean_" + path;
	}
}
