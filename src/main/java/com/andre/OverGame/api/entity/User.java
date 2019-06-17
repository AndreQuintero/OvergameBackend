package com.andre.OverGame.api.entity;

import java.util.Date;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.andre.OverGame.api.enums.ProfileEnum;

@Document
public class User {

	@Id
	private String id;
	
	@Indexed(unique = true)
	private String email;
	
	@Indexed(unique = true)
	private String username;
	
	@Size(min=6)
	private String password;
	
	 private ProfileEnum profile;
	
	private String description;
	
	private String avatar;
	
	private Date createdBy;
	
	private Date dataNascimento; 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ProfileEnum getProfile() {
		return profile;
	}

	public void setProfile(ProfileEnum profile) {
		this.profile = profile;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Date createdBy) {
		this.createdBy = createdBy;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	
}
