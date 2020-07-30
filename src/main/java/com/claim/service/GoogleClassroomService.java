package com.claim.service;

import com.claim.model.TSAssignment;
import com.claim.model.TSClass;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.*;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.classroom.Classroom;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class GoogleClassroomService implements CoursesService {
	
	//most of this came from https://developers.google.com/classroom/quickstart/java
	private static final String APPLICATION_NAME = "TeachSmart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "com.claim.model.tokens";
    //private static final List<String> SCOPES = Collections.singletonList(ClassroomScopes.CLASSROOM_COURSES_READONLY);
    private static final List<String> SCOPES = Arrays.asList(ClassroomScopes.CLASSROOM_COURSES_READONLY, ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS_READONLY, SheetsScopes.SPREADSHEETS_READONLY); 
    private static final String CREDENTIALS_FILE_PATH = "/client_secret.json";
    
    
    
    
    /**
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
    
    private List<Course> getCourses() throws GeneralSecurityException, IOException{
    	// Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Classroom service = new Classroom.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        // List the first 10 courses that the user has access to.
        ListCoursesResponse response = null;
		try {
			response = service.courses().list()
			        .setPageSize(10)
			        .execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        List<Course> courses = response.getCourses();
        if (courses == null || courses.size() == 0) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Courses:");
            for (Course course : courses) {
                System.out.printf("%s\n", course.getName());
            }
        }
        return courses;
    }  

	@Override
	public List<TSClass> listClasses(){
		List<Course> courses = null;
		try {
			courses = getCourses();
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (courses == null) {
			return null;
		}
		List<TSClass> classes = new ArrayList<TSClass>();
		for (Course course : courses) {
            TSClass thisClass = new TSClass();
            thisClass.setClassId(Long.parseLong(course.getId()));
            thisClass.setClassName(course.getName());
            classes.add(thisClass);
        }
	    return classes;
	}

	@Override
	public List<TSAssignment> listAssignments(long courseId) {
		List<TSAssignment> assignments = new ArrayList<TSAssignment>();
		return assignments;
	}

}
