package com.socialinspectors.storer.database.pattern;

import org.bson.Document;

public interface Pattern {
	/**
	 * Prepares {@link Document} document for mongodb insertions.
	 * 
	 * @return filled document.
	 */
	public Document getDocument();
}
