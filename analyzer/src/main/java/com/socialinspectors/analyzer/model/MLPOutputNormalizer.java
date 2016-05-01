package com.socialinspectors.analyzer.model;

import com.socialinspectors.analyzer.technique.senticnet.AnalysisResultHolder;

public class MLPOutputNormalizer {

	/**
	 * Normalizes output of the multi layer network using inputs being adjective
	 * and verb polarities.
	 * 
	 * @param result
	 * @param mlpScore
	 * @return Normalized output.
	 */
	public double normalize(AnalysisResultHolder result, double mlpScore) {
		if (mlpScore > 0) {
			if (result.getAdjPolarity() < 0) {
				if (result.getVerbPolarity() == 0 || result.getVerbPolarity() < 0) {
					return mlpScore * -1;
				}
			} else if (result.getAdjPolarity() == 0) {
				if (result.getVerbPolarity() < 0) {
					return mlpScore * -1;
				}

			}
		} else if (mlpScore < 0) {
			if (result.getAdjPolarity() > 0) {
				if (result.getVerbPolarity() > 0 || result.getVerbPolarity() == 0) {
					return mlpScore * -1;
				}

			} else if (result.getAdjPolarity() == 0) {
				if (result.getVerbPolarity() > 0) {
					return mlpScore * -1;
				}
			}
		}
		return mlpScore;

	}

}
