package com.claim.service;

import com.claim.model.IdToken;
import com.claim.model.TSAssignment;
import com.claim.model.TSClass;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.*;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.classroom.Classroom;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

@Component
public class GoogleClassroomService implements CoursesService {
	
	//most of this came from https://developers.google.com/classroom/quickstart/java
	private static final String APPLICATION_NAME = "TeachSmart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "com.claim.model.tokens";
    //private static final List<String> SCOPES = Collections.singletonList(ClassroomScopes.CLASSROOM_COURSES_READONLY);
    private static final List<String> SCOPES = Arrays.asList(ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS_READONLY, SheetsScopes.SPREADSHEETS_READONLY); 
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";
    private RestTemplate restTemplate = new RestTemplate();
    
    
    /**
     * PROBABLY WON'T NEED THESE TWO NOW THAT I'M JUST USING THE AUTH TOKENS
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleClassroomService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }  
    

    private List<Course> getCourses(String authToken){

    	final String uri = "https://classroom.googleapis.com/v1/courses?access_token="+authToken;
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	System.out.println("TOKE ::::> "+authToken);
    	//OAuth 2 access token
//    	headers.setBearerAuth(authToken);
    	//access_token

    	HttpEntity<String> entity = new HttpEntity<String>(headers);

        
    	ResponseEntity<ListCoursesResponse> response = restTemplate.getForEntity(uri, ListCoursesResponse.class);
    	
    	return response.getBody().getCourses();
    	
    }
    	

    
	@Override
	public List<Course> listClasses(String authToken){
		return  getCourses(authToken);
//		if (courses == null) {
//			return null;
//		}
//		List<TSClass> classes = new ArrayList<TSClass>();
//		for(int i=0; i< courses.size(); i++) {
////			Course course = new ObjectMapper().convertValue( courses.get(i).get("id"), Course.class);
//			try {
//	            TSClass thisClass = new TSClass();
//	            thisClass.setClassId(Long.parseLong((String)courses.get(i).get("id")));
//	            thisClass.setClassName((String)courses.get(i).get("name"));
//	            classes.add(thisClass);
//			}catch(Exception e) {
//				System.out.println("Failed to retrieve course ::> "+i);
//			}
//        };
//	    return classes;
	}

	private List<CourseWork> listAssignments(TSClass tSClass) {
		
		final String uri = "https://classroom.googleapis.com/v1/courses/" + tSClass.getClassId() + "/courseWork?access_token=" + tSClass.getAccessToken();
    	
    	RestTemplate restTemplate = new RestTemplate();
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	HttpEntity<String> entity = new HttpEntity<String>(headers);
        
    	ResponseEntity<ListCourseWorkResponse> response = restTemplate.getForEntity(uri, ListCourseWorkResponse.class);
    	
    	//so this is a java.util.LinkedHashMap
    	List<CourseWork> assignments = response.getBody().getCourseWork();
//    	forEach(BiConsumer(String, String))
    	
		
    	List<TSAssignment> tSAssignments = new ArrayList<TSAssignment>();
		/*
		 * ObjectMapper mapper = new ObjectMapper();
		 * mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		 * mapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
		 * mapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS,
		 * false);
		 * 
		 * //getting error here... java.util.LinkedHashMap cannot be cast to
		 * com.google.api.services.classroom.model.CourseWork
		 * 
		 * for (int j=0; j<assignments.size(); j++) { CourseWork assignment =
		 * mapper.convertValue(assignments.get(j), CourseWork.class); TSAssignment
		 * tSAssignment = new TSAssignment();
		 * tSAssignment.setAssignmentName(assignment.getTitle()); //TODO: work on
		 * setting up date
		 * 
		 * List<Material> materials = assignment.getMaterials(); for (int i=0;
		 * i<materials.size(); i++) { if (materials.get(i).getForm().getResponseUrl() !=
		 * null) { tSAssignment.setHasResponse(true);
		 * tSAssignment.setResponseUrl(materials.get(i).getForm().getResponseUrl());
		 * tSAssignment.setFormPictureUrl(materials.get(i).getForm().getThumbnailUrl());
		 * break; } }
		 * 
		 * tSAssignments.add(tSAssignment); }
		 */
		
		return assignments;
		
	}
	
	public List<CourseWork> listAssignmentsWithSheet(TSClass tSClass) {
		return listAssignments(tSClass);
		/*
		 * List<TSAssignment> assignmentsWithSheet = new ArrayList<TSAssignment>();
		 * 
		 * for (TSAssignment assignment : tSAssignments) { if
		 * (assignment.getHasResponse()) { assignmentsWithSheet.add(assignment); } }
		 * 
		 * return assignmentsWithSheet;
		 */
	}
	
	
//	public List<Sheet> getAssignmentResults(TSAssignment tSAssignment){
	public void getAssignmentResults(TSAssignment tSAssignment){
		String responseUrl = tSAssignment.getResponseUrl();
		
		String spreadSheetId = responseUrl.substring((responseUrl.indexOf("spreadsheets/d/")+15), responseUrl.indexOf("?usp="));
		System.out.println("spreadSheetId is:" + spreadSheetId);
		
    	final String uri = "https://sheets.googleapis.com/v4/spreadsheets/" + spreadSheetId  + "?access_token=" + tSAssignment.getAccessToken();
    	System.out.println("uri is:" + uri);
    	
    	RestTemplate restTemplate = new RestTemplate();
    	System.out.println("completed restTemplate");
    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    	System.out.println("completed headers");
    	
    	HttpEntity<String> entity = new HttpEntity<String>(headers);
    	System.out.println("completed Http");
    	
//    	ResponseEntity<Spreadsheet> response = restTemplate.getForEntity(uri, Spreadsheet.class);
    	LinkedHashMap<String, String> response = restTemplate.getForObject(uri, LinkedHashMap.class);
    	System.out.println("completed response");
    	
//    	return response.getBody().getSheets();
    	System.out.println(response);
//    	response.entrySet().forEach( entry -> {
//    	    System.out.println(entry.getKey() + "=>" + entry.getValue().getClass().toString());
//    	});
    	
    	//sheets is an array of linkedHashMaps?
//    	String sheets = response.get("properties");
    	System.out.println("Sheets are:" + response.get("properties"));
    	
    	//get(Object key)
    	
    	
    	
    	
    	
    	
    	
//    	ResponseEntity<ListCourseWorkResponse> response1 = restTemplate.getForEntity(uri, ListCourseWorkResponse.class);
//    	
//    	//so this is a java.util.LinkedHashMap
//    	List<CourseWork> assignments = response1.getBody().getCourseWork();
    	
    }



}
