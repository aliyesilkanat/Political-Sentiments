package com.socialinspectors.analyzer.technique;

public class UnexpectedTagIdException extends Exception {

	private static final long serialVersionUID = 5561128043619931785L;

	public UnexpectedTagIdException() {
		super("Unexpected tag Id was given to the context");
	}

}
