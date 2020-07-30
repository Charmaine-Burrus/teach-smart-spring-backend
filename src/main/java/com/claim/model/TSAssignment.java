package com.claim.model;

import java.util.Date;

public class TSAssignment {
	
	private String assignmentName;
	private Boolean hasResponse;
	private String responseUrl;
	private String formPictureUrl;
	private Date dueDate;
	
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
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	@Override
	public String toString() {
		return "Assignment [assignmentName=" + assignmentName + ", hasResponse=" + hasResponse + ", responseUrl="
				+ responseUrl + ", formPictureUrl=" + formPictureUrl + ", dueDate=" + dueDate + "]";
	}
	
}
