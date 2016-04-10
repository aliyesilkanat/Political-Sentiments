package com.socialinspectors.analyzer.model;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SenticNetModel {
	private final static Logger logger = LogManager.getLogger(SenticNetModel.class);

	private static SenticNetModel instance = new SenticNetModel();
	private ConcurrentHashMap<String, Double> polarityMap = null;

	private SenticNetModel() {
		polarityMap = loadMap();
	}

	/**
	 * Puts each concept and its polarity value into {@link ConcurrentHashMap}
	 * polarityMap
	 * 
	 * @return loaded {@link ConcurrentHashMap}
	 */
	private ConcurrentHashMap<String, Double> loadMap() {
		Model read = ModelFactory.createDefaultModel()
				.read(getClass().getClassLoader().getResourceAsStream("senticnet3.rdf.xml"), null);
		String query = "PREFIX : <http://example/> SELECT ?text ?polarity { ?s <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?o."
				+ "?s  <http://sentic.net/apitext> ?text." + "?s <http://sentic.net/apipolarity> ?polarity}";
		ResultSet execSelect = QueryExecutionFactory.create(QueryFactory.create(query), read).execSelect();

		ConcurrentHashMap<String, Double> map = new ConcurrentHashMap<String, Double>();
		while (execSelect.hasNext()) {
			QuerySolution querySolution = (QuerySolution) execSelect.next();
			map.put(querySolution.getLiteral("text").getString(), querySolution.getLiteral("polarity").getDouble());

		}
		return map;

	}

	public static SenticNetModel getInstance() {
		return instance;

	}

	public double getPolarity(String word) {
		double polarity = 0;
		if (polarityMap.containsKey(word)) {
			polarity = polarityMap.get(word);
			getLogger().debug("found concept in senticnet, word: {}, polarity {}", word, polarity);
		} else {
			getLogger().debug("cannot find given word in sentic.net model, word: {}", word);
		}
		return polarity;
	}

	public static Logger getLogger() {
		return logger;
	}

}
