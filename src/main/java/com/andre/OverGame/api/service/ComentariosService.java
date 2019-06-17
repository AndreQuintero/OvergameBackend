package com.andre.OverGame.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.andre.OverGame.api.entity.Comentarios;

@Component
public interface ComentariosService {

	void deleteComentarioById(String id);
	Page<Comentarios> findComentariosByVideoId(String videoId,int page, int count);
	Comentarios createOrUpdate(Comentarios comentario);
	Optional<Comentarios> findById(String id); 
}
