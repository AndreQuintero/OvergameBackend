package com.andre.OverGame.api.entity;


import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Video {

	@Id
	private String id;
	
	private String videoCaminho;
	
	@DBRef(lazy = true)
	private User user;
	
	private Date publicado;
	
	private Integer visualizacoes;
	
	private Integer likes;
	
	private Integer dislikes;
	
	@Transient
	private Page<Comentarios> comentarios;

	private String titulo;
	
	private String descricao;
	
	private String nomeJogo;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVideoCaminho() {
		return videoCaminho;
	}

	public void setVideoCaminho(String videoCaminho) {
		this.videoCaminho = videoCaminho;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getPublicado() {
		return publicado;
	}

	public void setPublicado(Date publicado) {
		this.publicado = publicado;
	}

	public Integer getVisualizacoes() {
		return visualizacoes;
	}

	public void setVisualizacoes(Integer visualizacoes) {
		this.visualizacoes = visualizacoes;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public Integer getDislikes() {
		return dislikes;
	}

	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}

	public Page<Comentarios> getComentarios() {
		
		return this.comentarios;
	}

	public void setComentarios(Page<Comentarios> comentarios) {
		this.comentarios = comentarios;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomeJogo() {
		return nomeJogo;
	}

	public void setNomeJogo(String nomeJogo) {
		this.nomeJogo = nomeJogo;
	}
	
	
	
}
