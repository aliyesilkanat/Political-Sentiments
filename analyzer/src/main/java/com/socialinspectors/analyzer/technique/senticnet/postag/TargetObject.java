package com.socialinspectors.analyzer.technique.senticnet.postag;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.IndexedWord;

public class TargetObject extends MeaningfulObject {
	private List<Adjective> adjectiveList;
	private IndexedWord modifiedObject = null;

	public TargetObject(IndexedWord modifiedObject, IndexedWord adjectiveWord, boolean foundNeg) {
		setAdjectiveList(new ArrayList<Adjective>());
		this.modifiedObject = modifiedObject;
		getAdjectiveList().add(new Adjective(foundNeg, adjectiveWord));

	}

	@Override
	public double getSentiment() {

		double totalSum = 0;
		for (Adjective adjective : getAdjectiveList()) {
			totalSum += adjective.getSentiment();
		}
		if (totalSum != 0) {
			totalSum = totalSum / getAdjectiveList().size();
		}
		return totalSum;
	}

	public IndexedWord getModifiedObject() {
		return modifiedObject;
	}

	public void setModifiedObject(IndexedWord modifiedObject) {
		this.modifiedObject = modifiedObject;
	}

	public void addNewAdjective(IndexedWord adjectiveWord, boolean foundNeg) {
		getAdjectiveList().add(new Adjective(foundNeg, adjectiveWord));

	}

	public List<Adjective> getAdjectiveList() {
		return adjectiveList;
	}

	public void setAdjectiveList(List<Adjective> adjectiveList) {
		this.adjectiveList = adjectiveList;
	}
}