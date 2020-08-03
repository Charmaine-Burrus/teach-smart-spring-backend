package com.claim.model;

public class TSAssignment {
	
	private String accessToken;
	private String assignmentName;
	private Boolean hasResponse;
	private String responseUrl;
	private String formPictureUrl;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAssignmentName() {
		return assignmentName;
	}
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
	public Boolean getHasResponse() {
		return hasResponse;
	}
	public void setHasResponse(Boolean hasResponse) {
		this.hasResponse = hasResponse;
	}
	public String getResponseUrl() {
		return responseUrl;
	}
	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	public String getFormPictureUrl() {
		return formPictureUrl;
	}
	public void setFormPictureUrl(String formPictureUrl) {
		this.formPictureUrl = formPictureUrl;
	}
	@Override
	public String toString() {
		return "TSAssignment [accessToken=" + accessToken + ",\nassignmentName=" + assignmentName + ", hasResponse="
				+ hasResponse + ", responseUrl=" + responseUrl + ", formPictureUrl=" + formPictureUrl + "]";
	}

	
	
}
