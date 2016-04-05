package com.socialinspectors.analyzer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.socialinspectors.analyzer.technique.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.SenticnetPolarityCalculator;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class MovieReviewBySentences {
	private static final String COMMENTS = "real:Negative,actual:Positive---the kids in the hall are an acquired taste . it took at least a season of watching their show on hbo before i became a believer . maybe after watching a half dozen kids in the hall movies , they would grow into the big screen . my recommendation is that , unless you are a big fan of the kids , skip the film . as it is , their first--and most likely only--attempt at a full length film lacks the qualities that made their comedy work on tv . a big-budget and glossy production can not make up for a lack of spontaneity that permeates their tv show . the kids go through the motions , but you get the feeling that they arent really having fun doing so . and this makes it more difficult for the audience to enjoy their antics . brain candy is a bunch of skits tied together by the story of a pharmaceutical company that develops a new drug to cure depression . in typical sketch-comedy tradition , each actor plays several roles . doctor cooper ( kevin mcdonald ) and his team create the drug . then , under pressure from don roritor ( mark mckinney ) , founder and president of roritor pharmaceuticals , dr . cooper releases the drug into the marketplace . the ensuing distribution of the new happy pill throughout the populace drives the rest of the film . at about 90 minutes , brain candy still seems long . the best thing about sketch comedy--and the kids are no exception--is the ability to quickly deliver the laughs , then go on to another quick skit . but with the additional set-up necessary in telling a longer , coherent story , the laughs just dont come fast enough . strangely , the show is even more tame than it was when on cable tv . the movie makes several attempts at risqueness--mostly by pointing up the gayness of one of scott thompsons characters--but they seem almost forced ; as if they have to live up to a pg rating . one of the best bits , though , does make use of thompsons naked buttocks ; we see him charging into battle--going to have sex with some guys taking a shower . in the classic of this genre , monty python pulled off this delicate balancing act between plot advancement and punchline delivery for most of the holy grail . the kids , unfortunately , are not up to the task . there are some amusing moments , to be sure , but not enough to make the experience an enjoyable one . \r\n\r\n\r\nskip the film \r\n\r\ncan not make up for a lack of spontaneity that permeates their tv show \r\n\r\nbut you get the feeling that they arent really having fun doing so \r\n\r\nunfortunately , are not up to the task . there are some amusing moments , to be sure , but not enough to make the experience an enjoyable one . \r\n\r\n----\r\n\r\nreal:Negative,actual:Positive---john boorman's \" zardoz \" is a goofy cinematic debacle so fundamentally misconceived and laughably executed that it takes on a bizarre enjoyment quality all its own . not since the rampant bumblings of one edward d . wood jr . has a movie been so silly and so serious at the same time . of course , wood's career can be explained by two things : he had no money and he had no talent . boorman , on the other hand , cannot court such excuses to explain \" zardoz \" ( or his follow-up film , the equally awful \" exorcist ii : the heretic \" ) . boorman obviously had a sizable budget , a matinee idol movie star ( sean connery ) in the lead role , and although you wouldn't know it from this film , boorman does indeed have talent . this is the man who made the slick modern masterpiece \" deliverance \" ( 1972 ) , as well as the autobiographical world war ii drama \" hope and glory \" ( 1987 ) , the slightly over-conceived arthurian epic \" excalibur \" ( 1981 ) and the father-son jungle adventure \" the emerald forest \" ( 1985 ) . his films all show that boorman is never lacking in imagination , but sometimes that comes at the cost of coherence and taste . if boorman is anything , he's ambitious , and when he succeeds , it's in grand fashion . unfortunately , the bigger they are , the harder they fall , and when boorman falls , the resounding impact can be heard for miles around .  \" zardoz \" is meant to takes its place among the grandest of mystical movies , an obsession of boorman's . his screenplay tries to elicit the same mythological connotations of the arthurian legends or even \" the wizard of oz , \" a book which figures into the movie's plot . but , despite all this reaching , the resulting movie is more unintentionally funny than intentionally enigmatical or compelling . the events take place in the distant year 2293 , but there is little of the typical futuristic movie-ness to be found . in fact , things seems to have moved backwards , with people riding horses , shooting old-style guns , and living in large victorian mansions . it's more middle ages than space age . the world of \" zardoz \" is divided into two distinct hemispheres : the outlands , where all the poor , pathetic people live , and the vortex , where a select group of wealthy intellectuals live in comfort and everlasting life . these immortals never grow old , they never engage in sexual activity , they possess psychological powers , and they live in a sort of quasi-utopian marxist society where everyone is equal , and everyone contributes equally to the society . however , if one breaks the rules , that person is punished by being aged so many years . if someone breaks the rules enough , he or she is aged to the point of senility , and imprisoned to an eternal existence in a geriatric home with others aged criminals . one of the immortals , arthur frayn ( niall buggy ) , a squirmy man with a mustache and goatee tattooed on his face , is charged with keeping order in the outlands and forcing the residents to farm so the immortals can be fed . like \" the wizard of oz , \" he adopts a god-like status among the people by flying in to their part of the world in a giant stone carved like a menacing head .  ( this flying head is one of the movie's opening images , and it's a dead giveaway of the lunacy to come . ) calling himself zardoz , frayn gathers a bunch of outlanders and makes them into a group called the exterminators , whose purpose is to kill most of the other outlanders so they can't procreate and take up more resources . from inside his giant , stone head , zardoz bellows seriously laugh-inducting statements like , \" the gun is good . the penis is evil . \" that line alone is worth the movie's cult following . one day , an exterminator named zed ( sean connery ) , sneaks into zardoz's flying stone , pushes frayn out , and goes back to the vortex . once there , the immortals label him a \" brutal \" and study him like a lab rat , taking great , perverse care in exploring his sexuality , which is a mystery to them . they seem especially interested in his ability to gain an erection , and there is one downright hilarious sequence where a bunch of scantily-clad female scientists show zed erotic footage on a video screen in an attempt to determine what gets him worked up . i say \" hilarious \" because that is exactly what \" zardoz \" is . it is obvious that boorman did not intend it to be so ; he made this film with the straightest of faces , although i have a hard time believing that as production moved forward , he didn't get even the slightest inkling of how patently ridiculous it was becoming . just looking at connery is enough to give one the giggles - he spends most of the film running around in a red loin cloth that resembles a diaper , a mane of hair braided halfway down his back , a wyatt earp-style handlebar mustache , and a pair of thigh-high patent leather boots that would look more appropriate on a cheap hollywood hooker . boorman made the film right after the critical and financial success of \" deliverance , \" which is the only reason i can imagine a studio would green-light this effort . he attracted some rich talent on both sides of the camera , including cinematographer geoffrey unsworth ( \" 2001 \" ) , whose striking visuals are about the only good thing in \" zardoz \" besides the inadvertent humor . sean connery had made his last james bond film in 1971 , and perhaps he was looking for a change in pace . he got exactly that in \" zardoz , \" and it's a wonder it didn't end his career . i'm sure boorman intended for this movie to make some grand statements . is it a treatise about the infallibility of eternal life ? is it a condemnation of those who consider growing old to be a bad thing ? or is it a social statement , something about the inherent negativity of class distinctions and the violence it creates ? karl marx might like it if he were more like timothy leary . come to think of it , maybe boorman made it as an extended lsd trip . people high on illicit substances are the only ones i can imagine enjoying this asinine silliness as anything more than a completely unintentional comedy . \r\n\r\nnot since the rampant bumblings of one edward d . wood jr . has a movie been so silly and so serious at the same time \r\n\r\nawful\r\n\r\nunfortunately\r\n\r\nthis asinine silliness as anything more than a completely unintentional comedy .\r\n";
	final SenticnetPolarityCalculator calculator = new SenticnetPolarityCalculator();
	private static final String DELIMATER = "///////";
	ExecutorService pool = Executors.newFixedThreadPool(10);

	public static void main(String[] args) {
		try {
			new MovieReviewBySentences().run();
			System.out.println("Job done!");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void run() throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter("movie_results_by_sentences.csv"));
		writer.append("Comments");
		writer.append(DELIMATER);
		writer.append("Score");
		writer.append(DELIMATER + "\n");
		extractSentiment(calculator, writer);
		writer.flush();
		writer.close();
	}

	private void extractSentiment(final SenticnetPolarityCalculator calculator, final BufferedWriter writer)
			throws IOException {
		double score = 0;

		List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(COMMENTS)
				.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
			try {
				score = calculator.traverseSentences(sentences, new ConcurrentLinkedQueue<Double>());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					writer.append(sentence.toString());
					writer.append(DELIMATER);
					writer.append(Double.toString(score));
					writer.append(DELIMATER + "\n");
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
