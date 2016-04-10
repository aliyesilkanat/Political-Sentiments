package com.socialinspectors.storer.actor.dispatcher;

import com.socialinspectors.storer.actor.GuardianStorer;
import com.socialinspectors.utils.actor.Actor;
import com.socialinspectors.utils.actor.MessageDispatcher;
import com.socialinspectors.utils.messages.MessageParser;
import com.socialinspectors.utils.messages.actors.AnalyzerMessages;

public class GuardianStorerDispatcher implements MessageDispatcher {

	@Override
	public void dispatch(Object message, Actor actor) {
		GuardianStorer ref = (GuardianStorer) actor;
		ref.getLogger().debug("received message to storer");
		MessageParser messageParser = new MessageParser();
		String header = messageParser.parseHeader(message);

		if (header.equals(AnalyzerMessages.STORE.toString())) {
			ref.store(message);

		} else {
			ref.unhandled(message);
		}

	}
}