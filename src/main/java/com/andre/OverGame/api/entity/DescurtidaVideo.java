package com.andre.OverGame.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DescurtidaVideo {

	@Id
	private String id;
	
	@DBRef
	private User user; // usuário que descurtiu o video;
	
	@DBRef
	private Video video; // video que foi dado dislike

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
	
	
}
