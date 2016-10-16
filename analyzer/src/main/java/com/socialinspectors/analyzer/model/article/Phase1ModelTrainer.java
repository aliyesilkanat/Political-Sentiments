package com.socialinspectors.analyzer.model.article;

import java.io.IOException;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;

public class Phase1ModelTrainer extends ModelTrainer {
	
	private static final String PHASE = "phase1";
	private static MultiLayerNetwork  network;

	public Phase1ModelTrainer(String string) {
		super(string);
	}


	public static void main(String[] args) {
		try {
			new Phase1ModelTrainer(PHASE).train();
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
				network = new Phase1ModelTrainer(PHASE).train();
			} catch (IOException | InterruptedException e) {
				getLogger().fatal("cannot train adjective and verb multi layer network");
			}
		}

		return network;
	}

	

}
