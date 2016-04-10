package com.socialinspectors.retriever.twitter;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStreamListenerTest {
	private ArrayList<Status> statusList = new ArrayList<Status>();

	@Test
	public void testStatusFetching() throws Exception {
		new TwitterStreamBuilder().build(32.78, -96.82, createListener());
		waitForNewTweets();
		if (statusList.size() == 0) {
			Assert.fail();
		}
	}

	@Test
	public void checkLanguage() throws Exception {
		new TwitterStreamBuilder().build(32.78, -96.82, createListener());
		assertTrue(waitForNewTweetsContainsLanguage());
	}

	private boolean waitForNewTweetsContainsLanguage()
			throws InterruptedException {

		int iterationNumber = 30;
		while (iterationNumber-- != 0) {
			if (statusList.size() > 0
					&& statusList.get(statusList.size() - 1).getLang()
							.equals("en")) {
				return true;
			}
			Thread.sleep(1000);
		}
		return false;

	}

	@Test
	public void checkGeoLoc() throws Exception {
		new TwitterStreamBuilder().build(32.78, -96.82, createListener());
		assertTrue(waitForNewTweetsContainsGeoLoc());
	}

	private boolean waitForNewTweetsContainsGeoLoc()
			throws InterruptedException {
		int iterationNumber = 30;
		while (iterationNumber-- != 0) {
			if (statusList.size() > 0
					&& statusList.get(statusList.size() - 1).getGeoLocation() != null) {
				return true;
			}
			Thread.sleep(1000);
		}
		return false;
	}

	private void waitForNewTweets() throws InterruptedException {
		int iterationNumber = 30;
		while (iterationNumber-- != 0) {
			if (statusList.size() > 0) {
				break;
			}
			Thread.sleep(1000);
		}
	}

	private StatusListener createListener() {
		return new StatusListener() {

			@Override
			public void onException(Exception ex) {
				System.err.println(ex);
			}

			@Override
			public void onStatus(Status status) {
				statusList.add(status);
				System.out.println("fetching from twitter stream, status: "
						+ status.getText());

			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

			}

			@Override
			public void onStallWarning(StallWarning warning) {

			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {

			}

			@Override
			public void onDeletionNotice(
					StatusDeletionNotice statusDeletionNotice) {

			}
		};
	}
}
