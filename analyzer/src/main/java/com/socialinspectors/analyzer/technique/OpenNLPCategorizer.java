package com.socialinspectors.analyzer.technique;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.PlainTextByLineStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OpenNLPCategorizer implements TechniqueStrategy {

	private final Logger logger = LogManager.getLogger(getClass());
	private DocumentCategorizerME documentCategorizer;

	public OpenNLPCategorizer() throws IOException {
		documentCategorizer = createDocumentCategorizer();
	}

	public DocumentCategorizerME createDocumentCategorizer() throws IOException {
		DoccatModel model = null;
		try {

			model = trainModel();
		} catch (Exception e) {
			getLogger().fatal("cannot train document categorizer model", e);
			throw e;
		}
		return new DocumentCategorizerME(model);
	}

	private DoccatModel trainModel() throws IOException,
			UnsupportedEncodingException {
		DoccatModel model;
		int cutoff = 2;
		int trainingIterations = 30;
		model = DocumentCategorizerME
				.train("en",
						new DocumentSampleStream(
								new PlainTextByLineStream(
										new ByteArrayInputStream(
												"1\tWatching a nice movie\r\n0\tThe painting is ugly, will return it tomorrow...\r\n1\tOne of the best soccer games, worth seeing it\r\n1\tVery tasty, not only for vegetarians\r\n1\tSuper party!\r\n0\tToo early to travel..need a coffee\r\n0\tDamn..the train is late again...\r\n0\tBad news, my flight just got cancelled.\r\n1\tHappy birthday mr. president\r\n1\tJust watch it. Respect.\r\n1\tWonderful sunset.\r\n1\tBravo, first title in 2014!\r\n0\tHad a bad evening, need urgently a beer.\r\n0\tI put on weight again\r\n1\tOn today's show we met Angela, a woman with an amazing story\r\n1\tI fell in love again\r\n0\tI lost my keys\r\n1\tOn a trip to Iceland\r\n1\tHappy in Berlin\r\n0\tI hate Mondays\r\n1\tLove the new book I reveived for Christmas\r\n0\tHe killed our good mood\r\n1\tI am in good spirits again\r\n1\tThis guy creates the most awesome pics ever \r\n0\tThe dark side of a selfie.\r\n1\tCool! John is back!\r\n1\tMany rooms and many hopes for new residents\r\n0\tFalse hopes for the people attending the meeting\r\n1\tI set my new year's resolution\r\n0\tThe ugliest car ever!\r\n0\tFeeling bored\r\n0\tNeed urgently a pause\r\n1\tNice to see Ana made it\r\n1\tMy dream came true\r\n0\tI didn't see that one coming\r\n0\tSorry mate, there is no more room for you\r\n0\tWho could have possibly done this?\r\n1\tI won the challenge\r\n0\tI feel bad for what I did\t\t\r\n1\tI had a great time tonight\r\n1\tIt was a lot of fun\r\n1\tThank you Molly making this possible\r\n0\tI just did a big mistake\r\n1\tI love it!!\r\n0\tI never loved so hard in my life\r\n0\tI hate you Mike!!\r\n0\tI hate to say goodbye\r\n1\tLovely!\r\n1\tLike and share if you feel the same\r\n0\tNever try this at home\r\n0\tDon't spoil it!\r\n1\tI love rock and roll\r\n0\tThe more I hear you, the more annoyed I get\r\n1\tFinnaly passed my exam!\r\n1\tLovely kittens\r\n0\tI just lost my appetite\r\n0\tSad end for this movie\r\n0\tLonely, I am so lonely\r\n1\tBeautiful morning\r\n1\tShe is amazing\r\n1\tEnjoying some time with my friends\r\n1\tSpecial thanks to Marty\r\n1\tThanks God I left on time\r\n1\tGreateful for a wonderful meal\r\n1\tSo happy to be home\r\n0\tHate to wait on a long queue\t\t\r\n0\tNo cab available\r\n0\tElectricity outage, this is a nightmare\r\n0\tNobody to ask about directions\r\n1\tGreat game!\r\n1\tNice trip\r\n1\tI just received a pretty flower\r\n1\tExcellent idea\r\n1\tGot a new watch. Feeling happy\r\n0\tI feel sick\r\n0\tI am very tired\r\n1\tSuch a good taste \r\n0\tSuch a bad taste\r\n1\tEnjoying brunch\r\n0\tI don't recommend this restaurant\r\n1\tThank you mom for supporting me\r\n0\tI will never ever call you again\r\n0\tI just got kicked out of the contest\r\n1\tSmiling\r\n0\tBig pain to see my team loosing\r\n0\tBitter defeat tonight\r\n0\tMy bike was stollen\r\n1\tGreat to see you!\r\n0\tI lost every hope for seeing him again\r\n1\tNice dress!\r\n1\tStop wasting my time\r\n1\tI have a great idea\r\n1\tExcited to go to the pub\r\n1\tFeeling proud\r\n1\tCute bunnies\r\n0\tCold winter ahead\r\n0\tHopless struggle..\r\n0\tUgly hat\r\n1\tBig hug and lots of love\r\n1\tI hope you have a wonderful celebration\r\n0\tFuck you!\r\n0\tI hate this life.\r\n0 \tFuck my life.\r\n1\tI love sundays.\r\n0\tNo no no.\r\n0\tWhy this always happen to me. "
														.getBytes()), "UTF-8")),
						cutoff, trainingIterations);
		return model;
	}

	@Override
	public int analyze(String tweet) {
		getLogger().debug("analyzing tweet: {}", tweet);

		double[] outcomes = documentCategorizer.categorize(tweet);
		String category = documentCategorizer.getBestCategory(outcomes);

		int result = 2;
		String resultAsString = "negative";
		if (category.equalsIgnoreCase("" + AnalysisResultCodes.POSITIVE)) {
			result = 1;
			resultAsString = "positive";
		}
		getLogger().trace("analyzed tweet. result: {}, tweet: {}",
				resultAsString, tweet);

		return result;
	}

	public Logger getLogger() {
		return logger;
	}

}
