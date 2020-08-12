package com.claim.model;

public class IdToken {
	
	//this is the TokenId Google sends when we use GoogleLogin
	private String googleTokenId;
	private String email;
	private String firstName;
	private String lastName;
	private String pictureUrl;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}
	public String getGoogleTokenId() {
		return googleTokenId;
	}
	public void setGoogleTokenId(String googleTokenId) {
		this.googleTokenId = googleTokenId;
	}
	@Override
	public String toString() {
		return "IdToken [googleTokenId=" + googleTokenId + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", pictureUrl=" + pictureUrl + "]";
	}

}
