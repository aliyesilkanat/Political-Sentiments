package com.socialinspectors.analyzer.model;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.canova.api.records.reader.RecordReader;
import org.canova.api.records.reader.impl.CSVRecordReader;
import org.canova.api.split.FileSplit;
import org.deeplearning4j.datasets.canova.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.DataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class ParagraphMLP {
	private static final Logger logger = LogManager.getLogger(ParagraphMLP.class);
	int seed = 123;
	double learningRate = 0.005;
	int batchSize = 50;
	int nEpochs = 100;
	private static MultiLayerNetwork network = null;
	int numInputs = 3;
	int numOutputs = 2;
	int numHiddenNodes = 50;

	public static void main(String[] args) {
		try {
			new ParagraphMLP().train();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ParagraphMLP() {
	}

	public static MultiLayerNetwork getModel() {

		if (network == null) {
			try {
				network = new ParagraphMLP().train();
			} catch (IOException | InterruptedException e) {
				getLogger().fatal("cannot train adjective and verb multi layer network");
			}
		}

		return network;
	}

	MultiLayerNetwork train() throws IOException, InterruptedException {
		// Load the training data:
		RecordReader rr = new CSVRecordReader();
		rr.initialize(new FileSplit(new File("src/main/resources/mlp/paragraph_training_data.csv")));
		DataSetIterator trainIter = new RecordReaderDataSetIterator(rr, batchSize, 0, 2);
		// Load the test/evaluation data:
		RecordReader rrTest = new CSVRecordReader();
		rrTest.initialize(new FileSplit(new File("src/main/resources/mlp/paragraph_eval_data.csv")));
		DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest, batchSize, 0, 2);
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).iterations(1)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).learningRate(learningRate)
				.updater(Updater.NESTEROVS).momentum(0.9).list(2)
				.layer(0,
						new DenseLayer.Builder().nIn(numInputs).nOut(numHiddenNodes).weightInit(WeightInit.XAVIER)
								.activation("relu").build())
				.layer(1,
						new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD).weightInit(WeightInit.XAVIER)
								.activation("softmax").nIn(numHiddenNodes).nOut(numOutputs).build())
				.pretrain(false).backprop(true).build();

		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(1));

		for (int n = 0; n < nEpochs; n++) {
			model.fit(trainIter);
		}
		
		 double[] evalPoints = new double[3];
		 evalPoints[0] = 0.26249998807907104;
		 evalPoints[1] = 0.04500000178813934;
		 evalPoints[2] = 0.04500000178813934;
		 INDArray create = Nd4j.create(evalPoints);
		// INDArray output = model.output(create, false);
		// int[] predict = model.predict(create);
		// System.out.println(output.getDouble(0) + " " + output.getDouble(1));
		// System.out.println(predict[0] + " " + predict[1]);
		//
		// evalPoints[0] = 0.8830000162124634;
		// evalPoints[1] = -0.23499999940395355;
		// create = Nd4j.create(evalPoints);
		// output = model.output(create, false);
		// predict = model.predict(create);
		// System.out.println(output.getDouble(0) + " " + output.getDouble(1));
		// System.out.println(predict[0] + " " + predict[1]);
		//
		// evalPoints[0] = 0;
		// evalPoints[1] = 0.667;
		// create = Nd4j.create(evalPoints);
		// output = model.output(create, false);
		// predict = model.predict(create);
		// System.out.println(output.getDouble(0) + " " + output.getDouble(1));
		// System.out.println(predict[0] + " " + predict[1]);
		System.out.println("Evaluate model....");
		Evaluation eval = new Evaluation(numOutputs);
		while (testIter.hasNext()) {
			DataSet t = testIter.next();
			INDArray features = t.getFeatureMatrix();
			INDArray lables = t.getLabels();
			INDArray predicted = model.output(features, false);

			eval.eval(lables, predicted);

		}

		System.out.println(eval.stats());
		return model;
	}

	public static Logger getLogger() {
		return logger;
	}
}
