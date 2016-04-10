package com.socialinspectors.analyzer.calculations.util;

public class Comment {
	private String content;
	private String score;
	private String result;
	
	public Comment(String content, String score,String result){
		this.setContent(content);
		this.setScore(score);
		this.setResult(result);
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
