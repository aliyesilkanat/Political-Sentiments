package com.socialinspectors.storer.database.pattern;

import static org.junit.Assert.assertEquals;

import org.bson.Document;
import org.junit.Test;

import com.socialinspectors.storer.database.pattern.tweets.TaggedTweetsPattern;

public class TaggedTweetPatternTest {
	private static final int HAPPY = 1;
	private static final int OPEN_NLP_CATEGORIZER = 1;

	@Test
	public void testStatedCountryTweetPattern() throws Exception {

		Document document = new TaggedTweetsPattern(
				"aliyesilkanat",
				"Ali Yeşilkanat",
				"https://twitter.com/aliyesilkanat",
				"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg",
				"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave … on #SoundCloud",
				"https://twitter.com/aliyesilkanat/status/637643909645094912",
				"2015-08-29", 38.476909, 27.064476, "Turkey", "IZMIRSTATE",
				"Izmir",1, OPEN_NLP_CATEGORIZER, HAPPY).getDocument();
		String json = document.toJson();
		System.out.println(json);
		assertEquals(
				"{ \"author\" : { \"username\" : \"aliyesilkanat\", \"uri\" : \"https://twitter.com/aliyesilkanat\", \"screenname\" : \"Ali Ye\u015Filkanat\", \"profilePicture\" : \"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg\" }, \"tweet\" : \"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave \u2026 on #SoundCloud\", \"uri\" : \"https://twitter.com/aliyesilkanat/status/637643909645094912\", \"location\" : { \"x\" : 38.476909, \"y\" : 27.064476 }, \"date\" : \"2015-08-29\", \"country\" : \"Turkey\", \"city\" : \"IZMIRSTATE\", \"state\" : \"Izmir\", \"tag\" : { \"id\" : 1, \"result\" : 1 } }",
				json);
	}

	@Test
	public void testNonStatedCountryTweetPattern() throws Exception {
		Document document = new TaggedTweetsPattern(
				"aliyesilkanat",
				"Ali Yeşilkanat",
				"https://twitter.com/aliyesilkanat",
				"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg",
				"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave … on #SoundCloud",
				"https://twitter.com/aliyesilkanat/status/637643909645094912",
				"2015-08-29", 38.476909, 27.064476, "Turkey", "Izmir",
				OPEN_NLP_CATEGORIZER, HAPPY,1).getDocument();
		String json = document.toJson();
		System.out.println(json);
		assertEquals(
				"{ \"author\" : { \"username\" : \"aliyesilkanat\", \"uri\" : \"https://twitter.com/aliyesilkanat\", \"screenname\" : \"Ali Ye\u015Filkanat\", \"profilePicture\" : \"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg\" }, \"tweet\" : \"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave \u2026 on #SoundCloud\", \"uri\" : \"https://twitter.com/aliyesilkanat/status/637643909645094912\", \"location\" : { \"x\" : 38.476909, \"y\" : 27.064476 }, \"date\" : \"2015-08-29\", \"country\" : \"Turkey\", \"city\" : \"Izmir\", \"tag\" : { \"id\" : 1, \"result\" : 1 } }",
				json);
	}
}
