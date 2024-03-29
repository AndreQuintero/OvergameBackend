package com.andre.OverGame.api.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.security.jwt.JwtUserFactory;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userService.findByEmail(email);
		
		if(user == null) {
			throw new UsernameNotFoundException(String.format("No user found with '%s'", email));
		}else {
			return JwtUserFactory.create(user);
		}
	}

}
