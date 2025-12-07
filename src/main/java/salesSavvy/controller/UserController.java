package salesSavvy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import salesSavvy.dto.LoginData;
import salesSavvy.entity.User;
import salesSavvy.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

                         // Allow requests from any origin
@RestController                           // This annotation indicates that this class is a REST controller
public class UserController {
	
	@Autowired                          // This annotation is used to inject the UsersService bean into this controller
	UserService service;              // This service will handle the business logic for user operations
	
	@PostMapping("/signUp")           // This method handles POST requests to the /signUp endpoint
	public String signUp(@RequestBody User user) {
		//TODO: process POST request
		System.out.println(user);
		return service.addUser(user); 
	}
	
	
	@PostMapping("/signIn")
	public String signIn(@RequestBody LoginData data) {
		return service.validateUser(data); 
	}
	
}
