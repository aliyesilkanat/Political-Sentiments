package com.socialinspectors.utils.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MessageParser {
	public String parseHeader(Object message) {

		return new Gson().fromJson(message.toString(), JsonObject.class)
				.get(MessageProperties.HEADER.toString()).getAsString();

	}

	public JsonObject parseBodyAsJsonObject(Object message) {
		return new Gson().fromJson(message.toString(), JsonObject.class)
				.get(MessageProperties.BODY.toString()).getAsJsonObject();
	}
}
