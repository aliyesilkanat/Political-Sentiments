package com.socialinspector.presenter.servlets;

import java.io.IOException;

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
import com.socialinspector.presenter.data.CollectionConstants;
import com.socialinspector.presenter.data.MongoConnector;
import com.socialinspectors.utils.prop.OntologyProperties;

/**
 * Servlet implementation class GeneralPercentage
 */
@WebServlet("/GeneralPercentageServlet")
public class GeneralPercentageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GeneralPercentageServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MongoDatabase database = MongoConnector.getDatabase();
		long totalTweetCount = MongoConnector.getDatabase().getCollection(CollectionConstants.TWEETS).count();
		setResponse(response, setResultObject(fillResultArray(database, getRatiosOfPeople(database, totalTweetCount)),
				totalTweetCount));

	}

	private JsonObject setResultObject(JsonArray fillResultArray, long totalTweetCount) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add("values", fillResultArray);
		jsonObject.addProperty("count", totalTweetCount);
		return jsonObject;
	}

	private JsonArray fillResultArray(MongoDatabase database, float[] ratios) {
		MongoCursor<Document> iterator = database.getCollection(CollectionConstants.FETCHING_PEOPLE).find().iterator();
		JsonArray peopleArray = new JsonArray();
		for (int i = 0; i < ratios.length; i++) {
			Document document = iterator.next();
			JsonObject personObject = new JsonObject();
			personObject.addProperty("label", document.getString(OntologyProperties.FULL_NAME));
			personObject.addProperty("color", document.getString(OntologyProperties.COLOR));
			personObject.addProperty("value", (int) ratios[i]);
			peopleArray.add(personObject);
		}
		return peopleArray;
	}

	private void setResponse(HttpServletResponse response, JsonObject jsonObject) throws IOException {
		response.getWriter().print(jsonObject);
		response.getWriter().flush();
	}

	private float[] getRatiosOfPeople(MongoDatabase database, long totalTweetCount) {
		int peopleCount = Math.toIntExact(database.getCollection(CollectionConstants.FETCHING_PEOPLE).count());
		float[] ratios = new float[peopleCount];

		for (int i = 1; i <= ratios.length; i++) {
			ratios[i - 1] = ((float) database.getCollection(CollectionConstants.TWEETS)
					.count(new Document(CollectionConstants.MENTIONED_PERSON_ID, i)) / totalTweetCount) * 100;
		}
		return ratios;
	}

}
