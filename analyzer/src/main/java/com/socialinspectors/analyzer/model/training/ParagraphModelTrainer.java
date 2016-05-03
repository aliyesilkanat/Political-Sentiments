package com.socialinspectors.analyzer.model.training;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;

import com.socialinspectors.analyzer.technique.pipeline.CoreNlpPipeline;
import com.socialinspectors.analyzer.technique.pipeline.SentenceAnalysisPipeline;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

public class ParagraphModelTrainer {
	private static final CharSequence DELIMATER = ",";
	private ExecutorService threadPool = Executors.newFixedThreadPool(20);

	public static void main(String[] args) {
		try {
			new ParagraphModelTrainer().train();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void train() throws IOException {
		final SentenceAnalysisPipeline analysisPipeline = new SentenceAnalysisPipeline();
		BufferedWriter writer = new BufferedWriter(new FileWriter("dadasdasneg__paragraph_three_group_ann_input.csv"));
		// extractSentiment(writer,
		// getClass().getClassLoader().getResource("movie_reviews/pos"), 1,
		// analysisPipeline);
		extractSentiment(writer, getClass().getClassLoader().getResource("movie_reviews/neg"), 0, analysisPipeline);
		writer.flush();
	}

	private void extractSentiment(final BufferedWriter writer, URL resource, final int datasetScore,
			SentenceAnalysisPipeline analysisPipeline) throws IOException {

		// File[] files = new File(resource.getPath()).listFiles();
		// for (File file : files) {
		// {
		// final String comment =
		// FileUtils.readFileToString(file).replaceAll("\n", "");
		String comment = "it's a shame the execution of this concept falls very short of its premise . \r\nthe movie is lacking and most unsatisfying . \r\nosmosis jones is crude , gross , disgusting , and was directed by the farrelly brothers - the twisted siblings behind there's something about mary , me , myself and irene and a couple of other movies in which the humor is mostly targeted below the belt . \r\nnot that there's anything wrong with scatology . \r\ni bet even cavemen appreciated bathroom humor - such as it was . \r\nit's merely that with osmosis jones , the farrellys humor is too juvenile , too predictable . \r\nyou can almost foresee the puns just by viewing the part of the inner anatomy a sequence is drawn . \r\nosmosis jones looks like one of those old health class movies gone psycho . \r\nthe trouble is , the animation may please the very young , but the jokes may be over their heads , while the teen-age audience may find it too tame for their tastes . \r\nadults ? well , let's just say most will find it unappetizing . \r\nosmosis jones plays like a 100-minute infomercial for the eat healthy foods lobby . \r\nthe live action sequences revolve around frank ( bill murray ) , who seems to be the grungiest human being in the universe . \r\nhe continually looks as if he needs a shave and a shower . \r\nfrank , much to the consternation of his daughter , is a fast-food addict , eating anything - and everything - that can kill you . \r\nhe works at a zoo where the animals look cleaner - and presumably smell better - than he does . \r\nfrank's body is invaded by thrax , a lethal virus , after frank eats a hard-boiled egg that had fallen to the ground . \r\nit's not gross enough that frank picks the egg up from the dirt and plops it into his mouth . \r\nnope , the farrellys pile it on by first having frank wrestle a chimp for the egg , wresting it from the primate's mouth . \r\nlike i said , the gross meter tips the scales on this one . \r\nafter ingesting the egg , the movie begins its animated sequences . \r\nhere , osmosis jones ( voiced by chris rock ) , a renegade white blood cell is teamed with drix ( voiced by frasier's david hyde pierce ) , a 12-hour , painkiller cold capsule to battle thrax ( smoothly voiced by laurence fishburne ) . \r\nbasically , what we have is a cliched cop-buddy movie , rife with all the clich ? s of that genre . \r\nand this is why osmosis jones doesn't click . \r\nit merely falls back on tired , familiar conventions instead of creating new and exciting situations . \r\nthe jokes and puns are lame : osmosis searches out a snitch , a former flu virus . \r\nafter pumping him for information , drix tells osmosis , \" funny , he doesn't look fluish . \" \r\nand the jokes don't rise above that level . \r\nthe live action scenes are no better . \r\nfrank is such a slob , so unappealing that it is difficult to fathom how he ever married or even sired a child . \r\nhe's almost a bigger cartoon than the animated characters . \r\nosmosis jones is a movie that may be too violent for young children as thrax burns and dissolves blood cells right and left . \r\nthe animation is rather two-dimensional and flat . \r\nit lacks scope and depth . \r\nit is an unappealing movie that will leave you scratching your head , and maybe leaning toward a shower after you walk out of the theater . \r\n";
		List<CoreMap> sentences = CoreNlpPipeline.getPipeline().process(comment)
				.get(CoreAnnotations.SentencesAnnotation.class);
		try {

			if (sentences.size() == 1) {
				double extractSentiment = getSentimentOfSentence(analysisPipeline, sentences);
				write(writer, datasetScore, 0.0, 0.0, extractSentiment);
			} else if (sentences.size() == 2) {
				double score1 = getSentimentOfSentence(analysisPipeline, sentences);
				double score2 = getSentimentOfSentence(analysisPipeline, sentences);
				write(writer, datasetScore, 0.0, score1, score2);
			} else if (sentences.size() > 2) {
				ConcurrentLinkedQueue<Double> firstGroup = new ConcurrentLinkedQueue<Double>();
				ConcurrentLinkedQueue<Double> secondGroup = new ConcurrentLinkedQueue<Double>();
				ConcurrentLinkedQueue<Double> thirdGroup = new ConcurrentLinkedQueue<Double>();
				int division = (int) sentences.size() / 3;

				for (int i = 0; i < division; i++) {
					addSentimentScoreToList(analysisPipeline, sentences, firstGroup, i);
				}
				for (int i = division; i < division * 2; i++) {
					addSentimentScoreToList(analysisPipeline, sentences, secondGroup, i);
				}
				for (int i = division * 2; i < sentences.size(); i++) {
					addSentimentScoreToList(analysisPipeline, sentences, thirdGroup, i);
				}
				nullCheck(firstGroup);
				nullCheck(secondGroup);
				nullCheck(thirdGroup);
				write(writer, datasetScore, firstGroup.stream().mapToDouble(val -> val).average().getAsDouble(),
						secondGroup.stream().mapToDouble(val -> val).average().getAsDouble(),
						thirdGroup.stream().mapToDouble(val -> val).average().getAsDouble());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// }

		// }

	}

	private void addSentimentScoreToList(SentenceAnalysisPipeline analysisPipeline, List<CoreMap> sentences,
			ConcurrentLinkedQueue<Double> firstGroup, int i) {
		try {
			firstGroup.add(analysisPipeline.extractSentiment(sentences.get(i)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private double getSentimentOfSentence(SentenceAnalysisPipeline analysisPipeline, List<CoreMap> sentences) {
		double score = 0.0;
		try {
			score = analysisPipeline.extractSentiment(sentences.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return score;
	}

	private void nullCheck(ConcurrentLinkedQueue<Double> firstGroup) {
		if (firstGroup.size() == 0) {
			firstGroup.add(0.0);
		}
	}

	private synchronized void write(final BufferedWriter writer, int datasetSentiment, double score1, double score2,
			double score3) throws IOException {
		writer.append(datasetSentiment + "");
		writer.append(DELIMATER);
		writer.append(Double.toString(score1));
		writer.append(DELIMATER);
		writer.append(Double.toString(score2));
		writer.append(DELIMATER);
		writer.append(Double.toString(score3));
		writer.append("\n");
		writer.flush();
	}
}
