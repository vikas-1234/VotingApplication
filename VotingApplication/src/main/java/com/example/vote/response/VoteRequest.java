package com.example.vote.response;

public class VoteRequest {
	private String emailId;
	private String password;
	private String selectedCandidate;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSelectedCandidate() {
		return selectedCandidate;
	}

	public void setSelectedCandidate(String selectedCandidate) {
		this.selectedCandidate = selectedCandidate;
	}

	@Override
	public String toString() {
		return "VoteRequest [emailId=" + emailId + ", password=" + password + ", selectedCandidate=" + selectedCandidate
				+ "]";
	}

}
