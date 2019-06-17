package com.andre.OverGame.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.andre.OverGame.api.entity.User;



@Component
public interface UserService {

	User findByEmail(String email);
	User findByUsername(String username);
	User findByUsernameOrEmail(String username, String email);
	User createOrUpdate(User user);
	Optional<User> findById(String id);
	void delete(String id);
	Page<User> findAll(int page, int count );
	List<User> findAllNoPaged();
}
