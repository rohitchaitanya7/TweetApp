package com.iiht.tweetapp.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iiht.tweetapp.exception.UserExistsException;
import com.iiht.tweetapp.model.AuthResponse;
import com.iiht.tweetapp.model.LoginDetails;
import com.iiht.tweetapp.model.UserData;
import com.iiht.tweetapp.service.UserServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/apps/v1.0/tweets")
@Tag(name="User Service Api",description = "Used for user login ,registration and user services")
public class UserServiceController {
	
	@Autowired
	private UserServices userService;
	@Operation(summary = "Registering Users",description = "A Post request for Registering User",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "201",description = "Successfully added the user"),
			@ApiResponse(responseCode = "400",description = "Input Validation Failed"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@PostMapping(value = "/register")
	public ResponseEntity<Object> register(@Valid @RequestBody UserData user) throws UserExistsException{
		return userService.register(user);
	}
	@Operation(summary = "Log in Users",description = "A Post request for Log in User",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Successfully logged in the user"),
			@ApiResponse(responseCode = "403",description = "Un-Successfully logged in the user"),
			@ApiResponse(responseCode = "400",description = "Input Validation Failed"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@PostMapping(value = "/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDetails userlogincredentials) {
		
		return userService.login(userlogincredentials);
	}
	@Operation(summary = "Getting all Users",description = "A get request for getting all Usera",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Successfully got all the users"),
			@ApiResponse(responseCode = "204",description = "No Users Found"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@GetMapping(value = "/users/all")
	public ResponseEntity<Object> getAllUsers(){
		return userService.getAllUsers();
	}
	@Operation(summary = "search User",description = "A get request for getting user with username",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Successfully got the users"),
			@ApiResponse(responseCode = "204",description = "No users Found"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@GetMapping(value = "/users/search/{username}")
	public ResponseEntity<Object> searchByUsername(@PathVariable String username){
		return userService.searchByUsername(username);
	}
	@Operation(summary = "Forgot password",description = "A Put request for Forgot Password",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Successfully reset password"),
			@ApiResponse(responseCode = "400",description = "Input Validation Failed"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@PutMapping(value="/{username}/forgot")
	public ResponseEntity<Object> forgotPassword(@PathVariable String username, @Valid @RequestBody LoginDetails data){
		return userService.forgotPassword(data);
	}
	@Operation(summary = "Validating Users",description = "A Get request for Validating User",tags = {"User Service Api"})
	@ApiResponses(value= {
			@ApiResponse(responseCode = "200",description = "Validated the user"),
			@ApiResponse(responseCode = "500",description = "Some Exception Occured")
	})
	@GetMapping(value = "/validate")
	public ResponseEntity<AuthResponse> getValidity(@RequestHeader("Authorization")final String token) {
		System.out.println("in side method {}"+token);
		return userService.validate(token);
	}
}
