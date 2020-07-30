package com.claim.service;

import com.claim.model.IdToken;

public interface TokenVerifier {
	
	IdToken verify(String tokenId);

}
