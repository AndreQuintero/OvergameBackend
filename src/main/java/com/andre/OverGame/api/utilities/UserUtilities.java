package com.andre.OverGame.api.utilities;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.security.jwt.JwtTokenUtil;
import com.andre.OverGame.api.service.UserService;

@Component
public class UserUtilities {

	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	public Boolean idsEquals(String usuarioLogadoId, String id) {
		
		if(usuarioLogadoId.equals(id)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public User userFromRequest(HttpServletRequest request) {
		String token = request.getHeader("Authorization"); //pega o token via header
		String email = this.jwtTokenUtil.getUsernameFromToken(token); // pega o email pelo token
		
		 return this.userService.findByEmail(email);
		
	}
}
