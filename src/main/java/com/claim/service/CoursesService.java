package com.claim.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.claim.model.TSAssignment;
import com.claim.model.TSClass;
import com.google.api.services.classroom.model.Course;

public interface CoursesService {
	
	//I think we'll probably need the access token as a param
//	List<TSClass> listClasses(String authToken);
	List<Course> listClasses(String authToken);
//	String authenticate() throws GeneralSecurityException, IOException;
	
	//probably need access token too (maybe acess token is a field and this is autowired?)
	//can list only those with Sheets by looping through and adding only those with hasResponse
	List<TSAssignment> listAssignments(String authToken, long classId);

}
