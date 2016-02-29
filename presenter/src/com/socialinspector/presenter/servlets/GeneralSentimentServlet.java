package com.socialinspector.presenter.servlets;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.socialinspector.presenter.data.AnalysisResultCodes;
import com.socialinspector.presenter.data.CollectionConstants;
import com.socialinspector.presenter.data.MongoConnector;
import com.socialinspectors.utils.prop.OntologyProperties;

/**
 * Servlet implementation class GeneralSentimentServlet
 */
@WebServlet("/GeneralSentimentServlet")
public class GeneralSentimentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MongoDatabase database = MongoConnector.getDatabase();
		float[] positiveRatiosOfPeople = getPositiveRatios(database);
		float[] negativeRatiosOfPeople = getNegativeRatios(database);
		MongoCursor<Document> iterator = database.getCollection(CollectionConstants.FETCHING_PEOPLE).find().iterator();
		JsonObject jsonObject = setResultObject(positiveRatiosOfPeople, negativeRatiosOfPeople, iterator);
		setResponse(response, jsonObject);
	}

	private JsonObject setResultObject(float[] positiveRatiosOfPeople, float[] negativeRatiosOfPeople,
			MongoCursor<Document> iterator) {
		JsonArray positivePeopleArray = new JsonArray();
		JsonArray negativePeopleArray = new JsonArray();
		iterateOverRatios(positiveRatiosOfPeople, negativeRatiosOfPeople, iterator, positivePeopleArray,
				negativePeopleArray);
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("positive", positivePeopleArray);
		jsonObject.add("negative", negativePeopleArray);
		return jsonObject;
	}

	private void iterateOverRatios(float[] positiveRatiosOfPeople, float[] negativeRatiosOfPeople,
			MongoCursor<Document> iterator, JsonArray positivePeopleArray, JsonArray negativePeopleArray) {
		for (int i = 0; i < positiveRatiosOfPeople.length; i++) {
			Document document = iterator.next();
			JsonObject negativePersonObject = fillPersonObject(negativeRatiosOfPeople, i, document);
			negativePeopleArray.add(negativePersonObject);
			JsonObject positivePersonObject = fillPersonObject(positiveRatiosOfPeople, i, document);
			positivePeopleArray.add(positivePersonObject);
		}
	}

	private JsonObject fillPersonObject(float[] negativeRatiosOfPeople, int i, Document document) {
		JsonObject negativePersonObject = new JsonObject();
		negativePersonObject.addProperty("label", document.getString(OntologyProperties.FULL_NAME));
		negativePersonObject.addProperty("color", document.getString(OntologyProperties.COLOR));
		negativePersonObject.addProperty("value", (int) negativeRatiosOfPeople[i]);
		return negativePersonObject;
	}

	private void setResponse(HttpServletResponse response, JsonObject jsonObject) throws IOException {
		response.getWriter().print(jsonObject);
		response.getWriter().flush();
	}

	private float[] getNegativeRatios(MongoDatabase database) {
		return getRatiosOfPeople(database, AnalysisResultCodes.NEGATIVE, AnalysisResultCodes.VERY_NEGATIVE);
	}

	private float[] getPositiveRatios(MongoDatabase database) {
		return getRatiosOfPeople(database, AnalysisResultCodes.POSITIVE, AnalysisResultCodes.VERY_POSITIVE);

	}

	private float[] getRatiosOfPeople(MongoDatabase database, int positive, int veryPositive) {
		Document filter = new Document("$or", Arrays.asList(
				new Document(OntologyProperties.TAG.concat(".").concat(OntologyProperties.TAG_RESULT), positive),
				new Document(OntologyProperties.TAG.concat(".").concat(OntologyProperties.TAG_RESULT), veryPositive)));
		int peopleCount = Math.toIntExact(database.getCollection(CollectionConstants.FETCHING_PEOPLE).count());
		float[] ratios = new float[peopleCount];
		long totalTweetCount = MongoConnector.getDatabase().getCollection(CollectionConstants.TWEETS).count(filter);
		for (int i = 1; i <= ratios.length; i++) {
			ratios[i - 1] = ((float) database.getCollection(CollectionConstants.TWEETS).count(new Document("$or",
					Arrays.asList(
							new Document(OntologyProperties.TAG.concat(".").concat(OntologyProperties.TAG_RESULT),
									positive),
							new Document(OntologyProperties.TAG.concat(".").concat(OntologyProperties.TAG_RESULT),
									veryPositive))).append(CollectionConstants.MENTIONED_PERSON_ID, i))
					/ totalTweetCount) * 100;
		}
		return ratios;
	}

}
