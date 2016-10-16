package com.socialinspectors.analyzer.model.article;

import java.io.IOException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class Phase2ModelTrainer extends ModelTrainer {

	private static final String PHASE = "phase2";
	static MultiLayerNetwork  network;

	public Phase2ModelTrainer(String string) {
		super(string);
	}


	public static void main(String[] args) {
		try {
			new Phase2ModelTrainer(PHASE).train();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static MultiLayerNetwork getModel() {
		
		if (network == null) {
			try {
				network = new Phase2ModelTrainer(PHASE).train();
			} catch (IOException | InterruptedException e) {
				getLogger().fatal("cannot train adjective and verb multi layer network");
			}
		}

		return network;
	}
}
