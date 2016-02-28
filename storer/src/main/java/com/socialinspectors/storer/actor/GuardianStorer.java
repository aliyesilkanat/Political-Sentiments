package com.socialinspectors.storer.actor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.ActorRef;
import akka.actor.Props;

import com.google.gson.JsonObject;
import com.socialinspectors.actor.Actor;
import com.socialinspectors.storer.StorerSystem;
import com.socialinspectors.storer.actor.dispatcher.GuardianStorerDispatcher;
import com.socialinspectors.storer.database.pattern.tweets.Json2TweetsPatternConverter;
import com.socialinspectors.utils.messages.MessageParser;

/**
 * @author Ali Yesilkanat.
 * 
 *         Class for Guardian Storer Actor
 *
 */
public class GuardianStorer extends Actor {
	public GuardianStorer() {
		super(new GuardianStorerDispatcher());
	}

	private final Logger logger = LogManager.getLogger(GuardianStorer.class
			.getName());

	public Logger getLogger() {
		return logger;
	}

	public void store(Object message) {
		JsonObject tweetObject = new MessageParser()
				.parseBodyAsJsonObject(message);
		ActorRef inserter = StorerSystem.system.actorOf(Props
				.create(Inserter.class));
		inserter.tell(new Json2TweetsPatternConverter().convert(tweetObject),
				getSelf());
	}
}
