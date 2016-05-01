package com.socialinspectors.analyzer.technique.senticnet;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class AnalysisResultHolder {
	private double adjPolarity;
	private double verbPolarity;

	public AnalysisResultHolder(double adjPolarity, double verbPolarity) {
		this.setAdjPolarity(adjPolarity);
		this.setVerbPolarity(verbPolarity);
	}

	public double getAdjPolarity() {
		return adjPolarity;
	}

	public void setAdjPolarity(double adjPolarity) {
		this.adjPolarity = adjPolarity;
	}

	public double getVerbPolarity() {
		return verbPolarity;
	}

	public void setVerbPolarity(double verbPolarity) {
		this.verbPolarity = verbPolarity;
	}

	public INDArray getINDArray() {
		return Nd4j.create(new double[] { adjPolarity, verbPolarity });

	}
}
