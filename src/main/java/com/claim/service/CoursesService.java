package com.claim.service;

import java.util.List;

import com.claim.model.TSAssignment;
import com.claim.model.TSClass;

public interface CoursesService {
	
	//I think we'll probably need the access token as a param
	List<TSClass> listClasses();
	
	//probably need access token too (maybe acess token is a field and this is autowired?)
	//can list only those with Sheets by looping through and adding only those with hasResponse
	List<TSAssignment> listAssignments(long classId);

}
