package com.socialinspectors.analyzer.model;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

public class Word2VecModel {
	private static final int NEAREST_WORDS_NUMBER = 10;
	private static final Word2VecModel instance = new Word2VecModel();
	private static final Logger logger = LogManager.getLogger(Word2VecModel.class);

	private Word2Vec vec;

	private Word2VecModel() {
		vec = WordVectorSerializer.loadFullModel("trainedMongoTweetsModel.txt");
	}

	public Collection<String> findNearest(String word) {
		return vec.wordsNearest(word, NEAREST_WORDS_NUMBER);

	}

	public static Word2VecModel getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return logger;
	}

}