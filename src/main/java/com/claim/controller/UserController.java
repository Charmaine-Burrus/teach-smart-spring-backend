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

import com.claim.model.User;
import com.claim.repository.UserRepository;

//Kenn & Hiram used @Controller instead... isn't that for MVC
//this is what I want to use since I don't have views right, since I'm using react... so I can now use @RequestMapping & @ResponseBody (to return object via JSON instead of returning a view)
@CrossOrigin
@RestController
public class UserController {
	
	
	//might use this instead if setup service -- private UsersService userService;
	private UserRepository userRepository;
	
	@Autowired
	public UserController(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	//Register - Create a new user with credentials  --I borrowed this logic from Lamar
	//@this is getting requests from browsers that request this URL, the request will send JSON values with user info.. it will be in the RequestBody since this is a post. We will save that info to our repo
	//doesn't look like we send anything back... front end will have to redirect itself to login page??
	@RequestMapping(value="/register", 
			consumes=MediaType.APPLICATION_JSON_VALUE,
			method=RequestMethod.POST)
	public void register(@RequestBody User user) {
		userRepository.save(user);
	}
	
	//Login - Returns user --I borrowed this logic from Lamar
	@RequestMapping(value="/login",
			consumes= MediaType.APPLICATION_JSON_VALUE,
			method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Optional<User>> login(@RequestBody User user) {
		Optional<User> u = userRepository.findByEmail(user.getEmail());
		
		if(u.isPresent() && u.get().getPassword().equals(user.getPassword())) {
			return new ResponseEntity<>(u, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
