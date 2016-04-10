package com.socialinspectors.utils.actor;

/**
 * @author Ali Yesilkanat
 *
 *         Class for dispatching guardian actors' actions.
 */
public interface MessageDispatcher {

	public void dispatch(Object message, Actor actor);

}
