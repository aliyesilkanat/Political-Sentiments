package com.socialinspectors.analyzer.actor.dispatcher;

import com.socialinspectors.analyzer.actor.GuardianAnalyzer;
import com.socialinspectors.utils.actor.Actor;
import com.socialinspectors.utils.actor.MessageDispatcher;
import com.socialinspectors.utils.messages.MessageParser;
import com.socialinspectors.utils.messages.actors.AnalyzerMessages;

public class GuardianAnalyzerDispatcher implements MessageDispatcher {

	@Override
	public void dispatch(Object message, Actor actor) {
		GuardianAnalyzer ref = (GuardianAnalyzer) actor;
		MessageParser parser = new MessageParser();
		String header = parser.parseHeader(message);
		if (header.equals(AnalyzerMessages.ANALYZE.toString())) {
			ref.analyze(parser.parseBodyAsJsonObject(message));
		}
	}

}
