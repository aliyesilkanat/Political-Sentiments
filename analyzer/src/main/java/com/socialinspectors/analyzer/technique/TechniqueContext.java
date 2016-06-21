package com.socialinspectors.analyzer.technique;

import java.io.IOException;

import com.socialinspectors.analyzer.technique.pipeline.TextAnalysisPipeline;

public class TechniqueContext {
	public static final int OPEN_NLP_CATEGORIZER = 1;
	public static final int STANFORD_CORE_NLP = 2;
	public static final int SENTICNET_CALCULATOR = 3;
	public static final int SENTICNET_FINAL_CALCULATOR = 4;

	public TechniqueContext(int id) throws UnexpectedTagIdException, IOException {
		if (id == OPEN_NLP_CATEGORIZER) {
			technique = new OpenNLPCategorizer();
		} else if (id == STANFORD_CORE_NLP) {
			technique = new CoreNLPSentiment();
		} else if (id == SENTICNET_FINAL_CALCULATOR) {
			technique = new TextAnalysisPipeline();
		} else {
			throw new UnexpectedTagIdException();
		}
	}

	public TechniqueStrategy getTechnique() {
		return technique;
	}

	private TechniqueStrategy technique;
}
