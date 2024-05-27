package com.example.vote.response;

public class VoteResponse {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "VoteResponse [message=" + message + "]";
	}

}
