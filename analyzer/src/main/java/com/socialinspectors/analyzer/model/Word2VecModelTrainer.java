package com.socialinspectors.analyzer.model;

import org.canova.api.util.ClassPathResource;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Word2VecModelTrainer {
	private static final String RAW_TEXT_FILE = "tweets_from_mongo.txt";

	public static void main(String[] args) {
		try {
			String filePath = new ClassPathResource(RAW_TEXT_FILE).getFile().getAbsolutePath();
			SentenceIterator iter = new BasicLineIterator(filePath);
			TokenizerFactory t = new DefaultTokenizerFactory();
			t.setTokenPreProcessor(new CommonPreprocessor());

			InMemoryLookupCache cache = new InMemoryLookupCache();
			WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>().vectorLength(100)
					.useAdaGrad(false).cache(cache).lr(0.025f).build();

			Word2Vec vec = new Word2Vec.Builder().minWordFrequency(5).iterations(1).epochs(1).layerSize(100).seed(42)
					.windowSize(5).iterate(iter).tokenizerFactory(t).lookupTable(table).vocabCache(cache).build();
			vec.fit();
			WordVectorSerializer.writeFullModel(vec, "trainedMongoTweetsModel.txt");

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}
