package com.socialinspectors.integration;

import java.util.Properties;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.socialinspectors.analyzer.technique.OpenNLPCategorizer;
import com.socialinspectors.analyzer.technique.TechniqueContext;
import com.socialinspectors.storer.database.MongoConnector;
import com.socialinspectors.storer.database.pattern.tweets.TweetsPatternConstants;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class CoreNlpResults {
	private static String[] emotions = { "very negative", "negative", "neutral", "positive", "very positive" };

	private int analyze(String tweet) {

		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
		int mainSentiment = 0;
		int longest = 0;
		Annotation annotation = pipeline.process(tweet);
		for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence.get(SentimentAnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			String partText = sentence.toString();
			if (partText.length() > longest) {
				mainSentiment = sentiment;
				longest = partText.length();
			}

		}
		return mainSentiment;
	}

	public static void main(String[] args) {
		new CoreNlpResults().traverse();
	}

	private void traverse() {
		MongoCollection<Document> mongoCollection = MongoConnector.getDatabase().getCollection("tweets");

		FindIterable<Document> iterable = mongoCollection.find();
		iterable.noCursorTimeout(false);
		MongoCursor<Document> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			try {

				Document document = (Document) iterator.next();
				String tweet = document.getString(TweetsPatternConstants.TWEET);
				String replaceAll = tweet.replaceAll("[^A-Za-z0-9()\\[\\]]", "");
				int analyze = analyze(replaceAll);
				if (analyze == 0 || analyze == 4)
					System.out.println(replaceAll + " " + emotions[analyze]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
