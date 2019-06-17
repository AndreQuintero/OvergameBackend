package com.andre.OverGame.api.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Comentarios {

	
	@Id
	private String id;
	
	@DBRef
	private Video video;
	
	@DBRef
	private User usuarioComment;
	
	private String mensagem;
	
	private Integer like;
	
	private Integer dislike;

	private Date publicado;
	
	private Date editado;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public User getUsuarioComment() {
		return usuarioComment;
	}

	public void setUsuarioComment(User usuarioComment) {
		this.usuarioComment = usuarioComment;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Integer getLike() {
		return like;
	}

	public void setLike(Integer like) {
		this.like = like;
	}

	public Integer getDislike() {
		return dislike;
	}

	public void setDislike(Integer dislike) {
		this.dislike = dislike;
	}

	public Date getPublicado() {
		return publicado;
	}

	public void setPublicado(Date publicado) {
		this.publicado = publicado;
	}

	public Date getEditado() {
		return editado;
	}

	public void setEditado(Date editado) {
		this.editado = editado;
	}
	
	
}
