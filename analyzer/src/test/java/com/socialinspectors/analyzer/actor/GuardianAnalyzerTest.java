package com.socialinspectors.analyzer.actor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.socialinspectors.utils.deployment.DeploymentContext;
import com.socialinspectors.utils.deployment.constants.SystemNames;
import com.socialinspectors.utils.prop.OntologyProperties;
import com.typesafe.config.ConfigFactory;

public class GuardianAnalyzerTest {
	private DeploymentContext deploymentContext;
	private ActorSystem system;
	private GuardianAnalyzer underlyingActor;

	@Before
	public void before() {
		deploymentContext = new DeploymentContext(
				new String[] { DeploymentContext.LOCALE }, SystemNames.ANALYZER);
		system = ActorSystem.create(SystemNames.ANALYZER,
				ConfigFactory.parseString(deploymentContext.getAkkaConfig()));
		Props props = Props.create(GuardianAnalyzer.class);
		TestActorRef<GuardianAnalyzer> ref = TestActorRef.create(system, props,
				"testGuardianAnalyzer");
		underlyingActor = ref.underlyingActor();
	}

	@Test
	public void testAnalyze() throws Exception {
		GuardianAnalyzer spy = Mockito.spy(underlyingActor);
		Mockito.doNothing().when(spy)
				.send2Storer(Mockito.any(JsonObject.class));

		spy.analyze(createJsonObejct("2015-09-08 00:04:00",
				"No but really, why",
				"https://twitter.com/benscottgtx/status/640993945007312897",
				"benscottgtx", "Lakeview 2-0",
				"https://twitter.com/benscottgtx", "Texas", "Dallas",
				32.7835222975902310, -96.8225097656250000));
		Mockito.verify(spy, Mockito.atLeastOnce())
				.send2Storer(
						new Gson()
								.fromJson(
										"{\"tweet\":\"No but really, why\",\"tweetUri\":\"https://twitter.com/benscottgtx/status/640993945007312897\",\"authorUri\":\"https://twitter.com/benscottgtx\",\"authorName\":\"Lakeview 2-0\",\"authorScreenName\":\"benscottgtx\",\"date\":\"2015-09-08 00:04:00\",\"city\":\"Texas\",\"state\":\"Dallas\",\"latitude\":32.78352229759023,\"longitude\":-96.822509765625,\"tag\":{\"id\":1,\"result\":2}}",
										JsonObject.class));
	}

	private JsonObject createJsonObejct(String date, String tweet,
			String tweetUri, String authorScreenName, String authorName,
			String authorUri, String city, String state, double latitude,
			double longitude) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(OntologyProperties.TWEET, tweet);
		jsonObject.addProperty(OntologyProperties.TWEET_URI, tweetUri);
		jsonObject.addProperty(OntologyProperties.AUTHOR_URI, authorUri);
		jsonObject.addProperty(OntologyProperties.AUTHOR_NAME, authorName);
		jsonObject.addProperty(OntologyProperties.AUTHOR_SCREEN_NAME,
				authorScreenName);
		jsonObject.addProperty(OntologyProperties.DATE, date);
		if (city != null) {
			jsonObject.addProperty(OntologyProperties.CITY, city);
		}
		if (state != null) {
			jsonObject.addProperty(OntologyProperties.STATE, state);
		}

		jsonObject.addProperty(OntologyProperties.LATITUDE, latitude);
		jsonObject.addProperty(OntologyProperties.LONGITUDE, longitude);
		return jsonObject;
	}

}
