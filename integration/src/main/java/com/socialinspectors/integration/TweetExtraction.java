package com.socialinspectors.integration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class TweetExtraction {
	public static void main(String[] args) {
		try {
			new TweetExtraction().run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void run() throws IOException {
		MongoClient mongoClient = new MongoClient("127.0.0.1", 27017);
		MongoCollection<Document> mongoCollection = mongoClient.getDatabase("social_inspectors")
				.getCollection("tweets");
		FindIterable<Document> iterable = mongoCollection.find();
		iterable.noCursorTimeout(true);
		MongoCursor<Document> iterator = iterable.iterator();
		BufferedWriter writer = new BufferedWriter(new FileWriter("tweets_from_mongo.txt"));
		while (iterator.hasNext()) {
			Document document = (Document) iterator.next();
			String tweet = document.getString("tweet");
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(tweet);
			stringBuilder.append("\n");
			writer.append(stringBuilder.toString());

		}
		writer.close();
		mongoClient.close();
	}
}
