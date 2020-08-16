package com.claim.service;

import com.claim.model.TSClass;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.*;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class GoogleClassroomService implements CoursesService {
    
	@Override
	public List<Course> listClasses(String authToken){
		final String uri = "https://classroom.googleapis.com/v1/courses?access_token="+authToken;
	    
		RestTemplate restTemplate = new RestTemplate();
	    	
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	
	    ResponseEntity<ListCoursesResponse> response = restTemplate.getForEntity(uri, ListCoursesResponse.class);
	    	
	    //here Jackson provides a java.util.LinkedHashMap representing a List<Course>
	    List<Course> classes = response.getBody().getCourses();	
	    return classes;
	}
	
	public List<CourseWork> listAssignments(TSClass tSClass) {
		final String uri = "https://classroom.googleapis.com/v1/courses/" + tSClass.getClassId() + "/courseWork?access_token=" + tSClass.getAccessToken();
	    	
	    RestTemplate restTemplate = new RestTemplate();
	    
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	        
	    ResponseEntity<ListCourseWorkResponse> response = restTemplate.getForEntity(uri, ListCourseWorkResponse.class);
	    	
	    //here Jackson provides a java.util.LinkedHashMap representing a List<CourseWork>
	    List<CourseWork> assignments = response.getBody().getCourseWork();   	
		return assignments;

	}
    	
}
