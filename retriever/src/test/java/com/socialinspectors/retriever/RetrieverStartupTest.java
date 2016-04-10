package com.socialinspectors.retriever;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

public class RetrieverStartupTest {

	private ActorSystem system;
	private ScheduledExecutorService executorService;

	@Before
	public void start() {
		// setup
		system = RetrieverSystem.system;
		executorService = Executors.newScheduledThreadPool(1);
	}

	@Test
	public void testSystemStart() throws Exception {

		// execute
		RetrieverSystem.main(null);

		// assert
		executorService.schedule(() -> assertEquals(true, system != null), 5,
				TimeUnit.SECONDS);
	}

	@Test
	public void testLeaderActorStart() throws Exception {
		// execute
		RetrieverSystem.main(null);

		// assert
		executorService.schedule(
				() -> {
					ActorSelection leaderRetrieverActor = system
							.actorSelection("Retriever.Leader");
					assertEquals(true, leaderRetrieverActor != null);
				}, 3, TimeUnit.SECONDS);
	}

	@After
	public void after() {
		system.shutdown();
	}

}
