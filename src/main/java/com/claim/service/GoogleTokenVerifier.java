package com.claim.service;

import java.util.Collections;

import com.claim.model.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

public class GoogleTokenVerifier implements TokenVerifier {
	
	private static final JacksonFactory jsonFactory = new JacksonFactory();

	@Override
	public IdToken verify(String idTokenString) {
		IdToken myIdToken = new IdToken();
		
		NetHttpTransport httpTransport = new NetHttpTransport();
		String clientId = "859167518630-2vfc35jchg1lndfmto5jolrvtsvf1kae.apps.googleusercontent.com";
		
		//make a request to the tokeninfo endpoint with the transport you give it and use the JSONFactory to create a parser to parse the response
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
				.setAudience(Collections.singletonList(clientId))
				.build();
				
				try {
					GoogleIdToken idToken = verifier.verify(idTokenString);
					if (idToken != null) {
						  Payload payload = idToken.getPayload();
						  
						  // Get profile information from payload & set myIdToken
						  myIdToken.setGoogleTokenId(idTokenString);
						  myIdToken.setEmail(payload.getEmail());
						  myIdToken.setLastName((String) payload.get("family_name"));
						  myIdToken.setFirstName((String) payload.get("given_name"));
						  myIdToken.setPictureUrl((String) payload.get("picture"));
						  
						  return myIdToken;
	
					} else {
						throw new Exception("Invalid ID token.");
					}
				}catch (Exception e) {
		            e.printStackTrace();
				}
		return null;
	}

}
