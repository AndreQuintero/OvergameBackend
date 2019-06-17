package com.andre.OverGame.api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DescurtidaComentario {

	@Id
	private String id;
	
	@DBRef
	private Comentarios comentario; // comentario que foi dado dislike
	
	@DBRef
	private User user; // usuario que descurtiu o comentario

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Comentarios getComentario() {
		return comentario;
	}

	public void setComentario(Comentarios comentario) {
		this.comentario = comentario;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
