package com.socialinspectors.retriever.actor.dispatcher;

import com.socialinspectors.actor.Actor;
import com.socialinspectors.actor.MessageDispatcher;
import com.socialinspectors.retriever.actor.GuardianRetriever;
import com.socialinspectors.utils.messages.MessageParser;
import com.socialinspectors.utils.messages.actors.RetrieverMessages;

public class GuardianRetrieverDispatcher implements MessageDispatcher {

	@Override
	public void dispatch(Object message, Actor actor) {
		GuardianRetriever ref = (GuardianRetriever) actor;
		if (new MessageParser().parseHeader(message).equals(RetrieverMessages.START.toString())) {
			ref.onStart();
		}

	}
}