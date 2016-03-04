package com.socialinspectors.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SenticnetClient {
	private static final String POLARITY = "/polarity";
	private static final String CONCEPT_URI = "http://sentic.net/api/en/concept/";
	private static final Logger logger = LogManager.getLogger(SenticnetClient.class);

	public double getConceptPolarity(String word) throws Exception {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("getting concept from senticnet, word: {}", word);
		}
		HttpClient client = createClient();
		String rawModelXml = client.newRequest(createUri(word)).send().getContentAsString();

		double extractedPolarity = extractPolarity(rawModelXml, word);

		if (getLogger().isTraceEnabled()) {
			getLogger().trace("got concept from senticnet, word: {}, polarity {}", word, extractedPolarity);
		}
		destroyClient(client);
		return extractedPolarity;

	}

	double extractPolarity(String rawModelXml, String word)
			throws ParserConfigurationException, SAXException, IOException {
		double polarity = 0;
		if (!rawModelXml.startsWith("The file")) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new ByteArrayInputStream(rawModelXml.getBytes(StandardCharsets.UTF_8)));
			document.getDocumentElement().normalize();
			String textContent = document.getElementsByTagName("polarity").item(0).getTextContent();
			polarity = Double.parseDouble(textContent);
		} else {
			// if Sentic net does not have given concept, we assume 0 as
			// polarity.
			getLogger().warn("sentic net does not have polarity data for word: {}", word);
			polarity = 0;
		}
		return polarity;
	}

	private String createUri(String word) {
		return CONCEPT_URI.concat(word).concat(POLARITY);
	}

	private void destroyClient(HttpClient client) throws Exception {
		client.stop();
		client.destroy();
	}

	private HttpClient createClient() throws Exception {
		HttpClient client = new HttpClient(new SslContextFactory(true));
		client.start();
		return client;
	}

	public static Logger getLogger() {
		return logger;
	}
}
