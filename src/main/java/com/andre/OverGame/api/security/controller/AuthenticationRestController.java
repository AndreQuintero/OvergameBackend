package com.andre.OverGame.api.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andre.OverGame.api.security.jwt.JwtTokenUtil;
import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.security.jwt.JwtAuthenticationRequest;
import com.andre.OverGame.api.security.model.CurrentUser;

@RestController
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private UserService userService;
    
    
 // cria o nosso token
    @PostMapping(value="/api/authorized")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {
    	
    
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = this.jwtTokenUtil.generateToken(userDetails);
        final User user = this.userService.findByEmail(authenticationRequest.getEmail());
        user.setPassword(null);
        return ResponseEntity.ok(new CurrentUser(token, user));
    }
    
    @PostMapping(value="/api/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = this.jwtTokenUtil.getUsernameFromToken(token);
        final User user = this.userService.findByEmail(username);
        
        if (this.jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = this.jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new CurrentUser(refreshedToken, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
