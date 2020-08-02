package com.claim.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.claim.model.IdToken;
import com.claim.model.TSAssignment;
import com.claim.model.TSClass;
import com.claim.model.User;
import com.claim.repository.UserRepository;
import com.claim.service.CoursesService;
import com.claim.service.GoogleClassroomService;
import com.claim.service.GoogleTokenVerifier;
import com.claim.service.TokenVerifier;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.sheets.v4.model.Sheet;

@CrossOrigin
@RestController
public class UserController {
	
	//might use this instead if setup service -- private UsersService userService;
	private UserRepository userRepository;
	CoursesService classroomService;
	
	@Autowired
	public UserController(UserRepository userRepository, CoursesService classroomService) {
		super();
		this.userRepository = userRepository;
		this.classroomService=classroomService;
	}

	@RequestMapping(value="/login",
			consumes= MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	@ResponseBody
	//we're basically making a temporary token here...
	public ResponseEntity<User> login(@RequestBody IdToken token) {
		if (token != null && token.getGoogleTokenId() != null) {
			//TO DO: probably want to autowire in the future
			TokenVerifier tokenVerifier = new GoogleTokenVerifier();
			IdToken idToken = tokenVerifier.verify(token.getGoogleTokenId());
			//check whether this user already exists
			User realUser = null;
			Optional<User> user = userRepository.findByEmail(idToken.getEmail());
			if (user.isPresent()) {
				realUser = user.get();
				realUser.setGoogleTokenId(idToken.getGoogleTokenId());
			}
			else {
				//Otherwise make a new user with Google info from the idToken //TO DO: why isn't this added to the database??
				realUser = new User();
				realUser.setEmail(idToken.getEmail());
				realUser.setFirstName(idToken.getFirstName());
				realUser.setLastName(idToken.getLastName());
				realUser.setPictureUrl(idToken.getPictureUrl());
				realUser.setGoogleTokenId(idToken.getGoogleTokenId());
				userRepository.save(realUser);
			}
			//return a response body with the user info
			return new ResponseEntity<>(realUser, HttpStatus.OK);
		}
		
		return null;
	}	
	
	@RequestMapping(value="/listCourses",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<List<Course>> listCourses(@RequestBody IdToken idToken){
		List<Course> courses = classroomService.listClasses(idToken.getGoogleTokenId());
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}	
	
	@RequestMapping(value="/listAssignmentsWithSheet",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<List<CourseWork>> listAssignmentsWithSheet(@RequestBody TSClass tSClass){
		List<CourseWork> assignments = classroomService.listAssignmentsWithSheet(tSClass);
		return new ResponseEntity<>(assignments, HttpStatus.OK);
	}	
	
	@RequestMapping(value="/addAssignmentResults",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	private void addAssignmentResults(@RequestBody TSAssignment tSAssignment){
//		List<Sheet> sheets = classroomService.getAssignmentResults(tSAssignment);
		GoogleClassroomService classroomService = new GoogleClassroomService();
		classroomService.getAssignmentResults(tSAssignment);
//		for (Sheet sheet : sheets) {
//			System.out.println(sheet);
//		}
	}
	
	
	//two basic gets for testing.... idk if I will actually use these in front end or not  --also borrowed from Lamar
	@RequestMapping(value="/findUserByEmail",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.GET)
	//we probably don't use @RequestBody in the parameter since this is a GET method... so if it's in the header, we can just pass it directly as a parameter??
	@ResponseBody
	private ResponseEntity<Optional<User>> findUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return new ResponseEntity<>(user, HttpStatus.OK);
		//but what about if there is no such user... don't we need to return an error like we did with login?  
		//or are we just sending back null b/c the actual request was good, we were able to execute what we were supposed to... and then if they do anything with user.. they will see that it's null
	}
	
	@RequestMapping(value="/findAllUsers",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.GET)
	@ResponseBody
	private ResponseEntity<List<User>>findAllUsers() {
		List<User> listOfUsers = userRepository.findAll();
		return new ResponseEntity<>(listOfUsers, HttpStatus.OK);
	}

}
