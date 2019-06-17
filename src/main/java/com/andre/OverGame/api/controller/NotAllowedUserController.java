package com.andre.OverGame.api.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.enums.ProfileEnum;
import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.api.utilities.UserUtilities;
import com.andre.OverGame.api.utilities.VideoUtilities;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/unauthorized/user")
@CrossOrigin(origins="*")
public class NotAllowedUserController {

	
	@Autowired
	private UserService userService;
	

	
	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private VideoUtilities videoUtilities;
	
	@PutMapping
	public ResponseEntity<Response<User>> updateUser(@RequestParam MultipartFile avatar,  @RequestParam String id, @RequestParam String email, @RequestParam String username,@RequestParam String description ,@RequestParam String dataNascimento ,HttpServletRequest request){
		Response<User> response = new Response<User>();
		User userLogado = this.userUtilities.userFromRequest(request);
		System.out.println("id "+ userLogado.getId());
		
		try {
			List<String> erros = new ArrayList<String>();
			validateUpdateUser(id,email,username,dataNascimento,erros);
			if(!userLogado.getId().equals(id)){
			
				 erros.add("Você não tem permissão para atualizar este usuário");
			}
			if(erros.size() > 0) {
				erros.forEach(error -> response.getErros().add(error));
			    return ResponseEntity.badRequest().body(response);
			}
			
			
			
			User user = new User();
			user.setId(userLogado.getId());
			user.setCreatedBy(userLogado.getCreatedBy());
			user.setProfile(userLogado.getProfile());
			user.setUsername(username);
			user.setPassword(userLogado.getPassword());
			user.setEmail(email);
			user.setDescription(description);
			if(dataNascimento.equals("notInformed")) {
				user.setDataNascimento(null);
			}else {
				user.setDataNascimento(stringToDate(dataNascimento)); 
			}
			String avatarString = this.videoUtilities.salvarAvatar(avatar);
			user.setAvatar(avatarString);
			
			User userPersisted = this.userService.createOrUpdate(user);
			userPersisted.setPassword(null);
			response.setData(user);
			
		}catch(Exception ex) {
			response.getErros().add(ex.getMessage()); 
			return ResponseEntity.badRequest().body(response);
		}
			return ResponseEntity.ok(response);
	}
	
	private Date stringToDate(String texto) {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		try {
			Date date = simpleDateFormat.parse(texto);
			return date;
		} catch (ParseException e) {
			
			e.printStackTrace();
			return null;
		}
	} 
	
	private void validateUpdateUser(String id, String email,String username, String dataNascimento, List<String> erros) {
		if(id == null) {
			erros.add("Id não informado");
		}
		if(email == null) {
			erros.add("Email não informado");
		}
		if(username == null) {
			erros.add("Username não informado");
		}
	}
	
	@DeleteMapping(value= "/{id}")
	public ResponseEntity<Response<String>> deleteUser(@PathVariable("id") String userId, HttpServletRequest request){
		User userLogado = this.userUtilities.userFromRequest(request);
		Response<String> response = new Response<String>();
		
		try {
			
			Optional<User> userOptional = this.userService.findById(userId);
			User user = userOptional.get();
			
			if(user == null) {
				response.getErros().add("Register not found id:" + userId);
				return ResponseEntity.badRequest().body(response);
			}
			
			if(userLogado.getProfile() == ProfileEnum.ROLE_ADMIN) {
				this.userService.delete(userId);
				return ResponseEntity.ok(response);
			}else {
				if(this.userUtilities.idsEquals(userLogado.getId(), userId)) {
					this.userService.delete(userId);
					return ResponseEntity.ok(response);
				}else {
					response.getErros().add("Você não tem permissão de excluir isso");
					return ResponseEntity.badRequest().body(response);
				}
			}
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
	}
	
	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<List<User>>> listAllUser(){
		
		Response<List<User>> response = new Response<List<User>>();
		
		
		try {
			List<User> users = this.userService.findAllNoPaged();
			response.setData(users);
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Response<Page<User>>> listUserPaged(@PathVariable int page, @PathVariable int count){
		Response<Page<User>> response = new Response<Page<User>>();
		Page<User> users = this.userService.findAll(page, count);
		response.setData(users);
		return ResponseEntity.ok(response);
	}
}
