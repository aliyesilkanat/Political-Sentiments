package com.socialinspectors.actor;

import akka.actor.UntypedActor;

/**
 * @author Ali Yesilkanat
 * 
 *         Wrapper {@link UntypedActor} class. Supports action dispatching with
 *         {@link MessageDispatcher}
 */
public abstract class Actor extends UntypedActor {
	protected MessageDispatcher dispatcher;

	public Actor(MessageDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void onReceive(Object message) throws Exception {
		dispatcher.dispatch(message, this);
	}

}
