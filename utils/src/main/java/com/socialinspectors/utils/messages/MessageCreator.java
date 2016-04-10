package com.socialinspectors.utils.messages;

import com.google.gson.JsonObject;

public class MessageCreator {

	/**
	 * Intentionally left blank.
	 */
	private MessageCreator() {

	}

	public static String create(Enum header, JsonObject body) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(MessageProperties.HEADER.toString(),
				header.toString());
		jsonObject.add(MessageProperties.BODY.toString(), body);
		return jsonObject.toString();

	}
	
}
