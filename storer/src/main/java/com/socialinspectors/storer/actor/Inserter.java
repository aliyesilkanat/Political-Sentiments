package com.socialinspectors.storer.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.UntypedActor;

import com.socialinspectors.storer.database.InsertQueries;
import com.socialinspectors.storer.database.pattern.tweets.TweetsPattern;

public class Inserter extends UntypedActor {
	private final Logger logger = LogManager
			.getLogger(Inserter.class.getName());

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof TweetsPattern) {
			TweetsPattern tweetsPattern = (TweetsPattern) message;
			getLogger().debug("received tweet insertion request, tweetUri: {}",
					tweetsPattern.getTweetUri());
			new InsertQueries().insertTweets(tweetsPattern.getDocument());
			getLogger().trace("inserted tweets to database, tweetUri: {}",
					tweetsPattern.getTweetUri());
		} else {
			unhandled(message);
		}

		getContext().stop(getSelf());
	}

	public Logger getLogger() {
		return logger;
	}

}
