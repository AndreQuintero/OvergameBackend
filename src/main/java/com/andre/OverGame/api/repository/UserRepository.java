package com.andre.OverGame.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.andre.OverGame.api.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);
	User findByUsername(String username);
	User findByUsernameOrEmail(String username, String email);
	
}
