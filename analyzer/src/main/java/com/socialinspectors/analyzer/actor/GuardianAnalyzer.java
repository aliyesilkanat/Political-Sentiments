package com.socialinspectors.analyzer.actor;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import akka.actor.ActorSelection;

import com.google.gson.JsonObject;
import com.socialinspectors.actor.Actor;
import com.socialinspectors.analyzer.AnalyzerSystem;
import com.socialinspectors.analyzer.actor.dispatcher.GuardianAnalyzerDispatcher;
import com.socialinspectors.analyzer.settings.AnalysisSettings;
import com.socialinspectors.analyzer.technique.TechniqueContext;
import com.socialinspectors.analyzer.technique.UnexpectedTagIdException;
import com.socialinspectors.utils.OntologyProperties;
import com.socialinspectors.utils.messages.MessageCreator;
import com.socialinspectors.utils.messages.actors.AnalyzerMessages;

/**
 * @author Ali Yesilkanat
 * 
 *         Guardian Actor class for Analyzer System.
 *
 */
public class GuardianAnalyzer extends Actor {

	private final Logger logger = LogManager.getLogger(GuardianAnalyzer.class.getName());

	public GuardianAnalyzer() {
		super(new GuardianAnalyzerDispatcher());
	}

	/**
	 * Analyze tweets and sends them with results to Storer.
	 * 
	 * @param messageBody
	 */
	public void analyze(JsonObject messageBody) {
		String tweet = messageBody.get(OntologyProperties.TWEET).getAsString();
		try {
			// get current analysis tag number.
			int tagId = AnalysisSettings.getSettings().getId();

			// get result of analysis
			int result = new TechniqueContext(tagId).getTechnique().analyze(tweet);

			JsonObject tagObject = prepareTagObject(tagId, result);
			// insert analysis results to tweet object.
			messageBody.add(OntologyProperties.TAG, tagObject);
			send2Storer(messageBody);

		} catch (UnexpectedTagIdException e) {
			getLogger().fatal("Unexpected tag id was found in AnalysisSettings, ID: {}",
					AnalysisSettings.getSettings().getId());
			unhandled(messageBody);
		} catch (IOException e) {
			getLogger().fatal("cannot train nlp model, message: {}", messageBody, e);
			unhandled(messageBody);
		}

	}

	void send2Storer(JsonObject messageBody) {
		ActorSelection receiverActor = getContext()
				.actorSelection(AnalyzerSystem.deploymentContext.getGuardianPaths().getStorer());
		receiverActor.tell(MessageCreator.create(AnalyzerMessages.STORE, messageBody), getSelf());
	}

	private JsonObject prepareTagObject(int tagId, int result) {
		JsonObject tagObject = new JsonObject();
		tagObject.addProperty(OntologyProperties.TAG_ID, tagId);
		tagObject.addProperty(OntologyProperties.TAG_RESULT, result);
		return tagObject;
	}

	public Logger getLogger() {
		return logger;
	}

}
