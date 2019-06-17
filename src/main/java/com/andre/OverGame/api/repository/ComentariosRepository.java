package com.andre.OverGame.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.andre.OverGame.api.entity.Comentarios;

public interface ComentariosRepository extends MongoRepository<Comentarios, String>{

	Page<Comentarios> findByVideoIdOrderByPublicadoDesc(String videoId, Pageable pages);
	
}
