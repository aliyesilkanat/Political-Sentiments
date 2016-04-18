package com.socialinspectors.analyzer.technique.senticnet;

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
}
