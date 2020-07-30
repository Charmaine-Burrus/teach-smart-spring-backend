package com.claim.model;

public class IdToken {
	
	//this is the String that comes from google for /login
	private String tokenId;
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
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	@Override
	public String toString() {
		return "IdToken [tokenId=" + tokenId + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", pictureUrl=" + pictureUrl + "]";
	}
	

}
