package com.claim.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.claim.model.TSAssignment;
import com.claim.model.TSClass;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.sheets.v4.model.Sheet;

public interface CoursesService {
	List<Course> listClasses(String authToken);
	
	List<CourseWork> listAssignments(TSClass tSClass);
	
//	List<Sheet> getAssignmentResults(TSAssignment tSAssignment);

}
