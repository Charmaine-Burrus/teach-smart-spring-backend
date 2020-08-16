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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.claim.model.IdToken;
import com.claim.model.TSClass;
import com.claim.model.User;
import com.claim.repository.UserRepository;
import com.claim.service.CoursesService;
import com.claim.service.GoogleTokenVerifier;
import com.claim.service.TokenVerifier;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;

@CrossOrigin
@RestController
public class UserController {
	
	private UserRepository userRepository;
	private CoursesService classroomService;
	
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
	public ResponseEntity<User> login(@RequestBody IdToken token) {
		if (token != null && token.getGoogleTokenId() != null) {
			TokenVerifier tokenVerifier = new GoogleTokenVerifier();
			IdToken idToken = tokenVerifier.verify(token.getGoogleTokenId());
			User realUser = null;
			Optional<User> user = userRepository.findByEmail(idToken.getEmail());
			if (user.isPresent()) {
				realUser = user.get();
				realUser.setGoogleTokenId(idToken.getGoogleTokenId());
			}
			else {
				realUser = new User();
				realUser.setEmail(idToken.getEmail());
				realUser.setFirstName(idToken.getFirstName());
				realUser.setLastName(idToken.getLastName());
				realUser.setPictureUrl(idToken.getPictureUrl());
				realUser.setGoogleTokenId(idToken.getGoogleTokenId());
				userRepository.save(realUser);
			}
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
	
	@RequestMapping(value="/listAssignments",
			produces=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	@ResponseBody
	private ResponseEntity<List<CourseWork>> listAssignmentsWithSheet(@RequestBody TSClass tSClass){
		List<CourseWork> assignments = classroomService.listAssignments(tSClass);
		return new ResponseEntity<>(assignments, HttpStatus.OK);
	}	

}
