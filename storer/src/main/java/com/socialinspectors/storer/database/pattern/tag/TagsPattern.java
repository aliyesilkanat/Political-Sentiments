package com.socialinspectors.storer.database.pattern.tag;

import org.bson.Document;

import com.socialinspectors.storer.database.pattern.Pattern;

public class TagsPattern implements Pattern {

	private String description;

	private int id;

	private String name;

	/**
	 * @param id
	 * @param name
	 * @param description
	 */
	public TagsPattern(int id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Document getDocument() {
		return new Document().append(TagsPatternConstants.ID, getId())
				.append(TagsPatternConstants.NAME, getName())
				.append(TagsPatternConstants.DESCRIPTION, getDescription());
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
