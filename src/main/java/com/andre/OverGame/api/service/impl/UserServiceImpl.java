package com.andre.OverGame.api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.repository.UserRepository;
import com.andre.OverGame.api.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRep;
	
	@Override
	public User findByEmail(String email) {
		
		return this.userRep.findByEmail(email);
	}

	@Override
	public User findByUsername(String username) {
		
		return this.userRep.findByUsername(username);
	}

	@Override
	public User findByUsernameOrEmail(String username, String email) {
		
		return this.userRep.findByUsernameOrEmail(username, email);
	}

	@Override
	public User createOrUpdate(User user) {
		
		return this.userRep.save(user);
	}

	@Override
	public Optional<User> findById(String id) {
		
		return this.userRep.findById(id);
	}

	@Override
	public void delete(String id) {
		
		this.userRep.deleteById(id);
	}

	@Override
	public Page<User> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.userRep.findAll(pages);
	}

	@Override
	public List<User> findAllNoPaged() {
		
		return this.userRep.findAll();
	}

}
