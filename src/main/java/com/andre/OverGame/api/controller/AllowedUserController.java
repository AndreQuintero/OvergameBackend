package com.andre.OverGame.api.controller;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.enums.ProfileEnum;
import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/authorized/user")
@CrossOrigin(origins="*")
public class AllowedUserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	private void validateCreateUser(User user, BindingResult result) {
		if(user.getEmail() == null) {
			result.addError(new ObjectError("User", "Email não informado"));
		} 
		
		if(user.getUsername() == null) {
			result.addError(new ObjectError("User", "Nome de usuário nçao informado"));
			
		}
		
		if(user.getPassword() == null) {
			result.addError(new ObjectError("User", "Senha não informada"));
		}
	}
	
	
	
	@PostMapping
	public ResponseEntity<Response<User>> createUser(@RequestBody User user, BindingResult result){
		
		Response<User> response = new Response<User>();
		
		try {
			validateCreateUser(user, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			user.setProfile(ProfileEnum.ROLE_CUSTOMER);
			user.setCreatedBy(new Date());
			user.setPassword(this.passwordEncoder.encode(user.getPassword()));
			user.setAvatar("avatar.jpg");
			user.setDataNascimento(null);
			user.setDescription("");
			
			User userPersisted = (User) this.userService.createOrUpdate(user);
			response.setData(userPersisted);
			
		}catch(DuplicateKeyException dE) {
			response.getErros().add("E-mail e/ou Nome de Usuário já registrados!");
			return ResponseEntity.badRequest().body(response);
		}
		catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="{id}")
	public ResponseEntity<Response<User>> findUserByID(@PathVariable String id){
		
		Response<User> response = new Response<User>();
		
		try {
			Optional<User> userOptional = this.userService.findById(id);
			User user = userOptional.get();		
			if(user == null) {
				response.getErros().add("Usuário não encontrado para o id: "+ id);
				return ResponseEntity.badRequest().body(response);
			}
			user.setPassword(null);
			response.setData(user);
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
}
