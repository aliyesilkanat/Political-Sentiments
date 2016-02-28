package com.socialinspectors.storer.database.pattern;

import static org.junit.Assert.assertEquals;

import org.bson.Document;
import org.junit.Test;

import com.socialinspectors.storer.database.pattern.tweets.NonTaggedTweetsPattern;

/**
 * Test Class for MongoDb Document Patterns
 * 
 * @author Ali Yeşilkanat
 *
 */
public class NonTaggedTweetsPatternTest {
	@Test
	public void testStatedCountryTweetPattern() throws Exception {
		Document document = new NonTaggedTweetsPattern(
				"aliyesilkanat",
				"Ali Yeşilkanat",
				"https://twitter.com/aliyesilkanat",
				"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg",
				"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave … on #SoundCloud",
				"https://twitter.com/aliyesilkanat/status/637643909645094912",
				"2015-08-29", 38.476909, 27.064476, "Turkey", "IZMIRSTATE",
				"Izmir",1).getDocument();
		String json = document.toJson();
		System.out.println(json);
		assertEquals(
				"{ \"author\" : { \"username\" : \"aliyesilkanat\", \"uri\" : \"https://twitter.com/aliyesilkanat\", \"screenname\" : \"Ali Ye\u015Filkanat\", \"profilePicture\" : \"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg\" }, \"tweet\" : \"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave \u2026 on #SoundCloud\", \"uri\" : \"https://twitter.com/aliyesilkanat/status/637643909645094912\", \"location\" : { \"x\" : 38.476909, \"y\" : 27.064476 }, \"date\" : \"2015-08-29\", \"country\" : \"Turkey\", \"city\" : \"Izmir\", \"state\" : \"IZMIRSTATE\" }",
				json);
	}

	@Test
	public void testNonStatedCountryTweetPattern() throws Exception {
		Document document = new NonTaggedTweetsPattern(
				"aliyesilkanat",
				"Ali Yeşilkanat",
				"https://twitter.com/aliyesilkanat",
				"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg",
				"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave … on #SoundCloud",
				"https://twitter.com/aliyesilkanat/status/637643909645094912",
				"2015-08-29", 38.476909, 27.064476, "Turkey", "Izmir",1)
				.getDocument();
		String json = document.toJson();
		System.out.println(json);
		assertEquals(
				"{ \"author\" : { \"username\" : \"aliyesilkanat\", \"uri\" : \"https://twitter.com/aliyesilkanat\", \"screenname\" : \"Ali Ye\u015Filkanat\", \"profilePicture\" : \"https://pbs.twimg.com/profile_images/2595463367/f4kuc43hjuq2n6ztf7zj_400x400.jpeg\" }, \"tweet\" : \"A new favorite: Hot Since 82 & Habischman - Leave Me (Moda Black) by @hotsince82 https://soundcloud.com/hotsince-82/hot-since-82-habischman-leave \u2026 on #SoundCloud\", \"uri\" : \"https://twitter.com/aliyesilkanat/status/637643909645094912\", \"location\" : { \"x\" : 38.476909, \"y\" : 27.064476 }, \"date\" : \"2015-08-29\", \"country\" : \"Turkey\", \"city\" : \"Izmir\" }",
				json);
	}

}
