package com.socialinspectors.retriever.actor;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.google.gson.JsonObject;
import com.socialinspectors.deployment.DeploymentContext;
import com.socialinspectors.deployment.constants.SystemNames;
import com.socialinspectors.retriever.settings.locations.FetchingLocationsHolder;
import com.socialinspectors.retriever.twitter.StatusExample;
import com.typesafe.config.ConfigFactory;

public class ExtractorTest {
	private TwitterExtractor underlyingActor;
	private ActorSystem system;
	private DeploymentContext deploymentContext;

	@Before
	public void before() {
		deploymentContext = new DeploymentContext(new String[] { DeploymentContext.LOCALE }, SystemNames.RETRIEVER);
		system = ActorSystem.create(SystemNames.RETRIEVER,
				ConfigFactory.parseString(deploymentContext.getAkkaConfig()));
		Props props = Props.create(TwitterExtractor.class);
		TestActorRef<TwitterExtractor> ref = TestActorRef.create(system, props, "testExtractorActor");
		underlyingActor = ref.underlyingActor();
	}

	@Test
	public void checkExtraction() throws Exception {

		// setup
		StatusExample example = new StatusExample();
		FetchingLocationsHolder settingsHolder = createMockSettings();

		// execution
		JsonObject extractedObject = underlyingActor.extractTweetData(example, settingsHolder, 1);

		// assertion
		assertEquals(
				"{\"tweet\":\"That Halep vs lisicki rally \uD83D\uDC4C\uD83C\uDFBE\",\"tweetUri\":\"https://twitter.com/joannas763/status/640966865909518336\",\"authorUri\":\"https://twitter.com/joannas763\",\"authorName\":\"joanna\",\"authorScreenName\":\"joannas763\",\"authorProfilePic\":\"https://pbs.twimg.com/profile_images/640639392416051200/wkSWNXPs.jpg\",\"date\":\"2015-10-07 22:16:24\",\"city\":\"Dallas\",\"state\":\"Texas\",\"latitude\":32.78352229759023,\"longitude\":-96.822509765625}",
				extractedObject.toString());
	}

	@After
	public void after() {
		system.shutdown();
	}

	private FetchingLocationsHolder createMockSettings() {
		FetchingLocationsHolder settingsHolder = new FetchingLocationsHolder();
		settingsHolder.setCity("Dallas");
		settingsHolder.setState("Texas");
		settingsHolder.setLatitude(32.78352229759023);
		settingsHolder.setLongitude(-96.822509765625);
		return settingsHolder;
	}
}
