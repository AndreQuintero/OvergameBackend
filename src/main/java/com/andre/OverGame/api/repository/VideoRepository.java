package com.andre.OverGame.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.andre.OverGame.api.entity.Video;

public interface VideoRepository extends MongoRepository<Video, String>{

	Page<Video> findByTituloContainingIgnoreCaseOrderByPublicadoDescVisualizacoesDesc(String titulo, Pageable pages); // apenas para teste
//	Page<Video> findByTituloContainingIgnoreCaseOrUserIdIgnoreCase(String titulo, String userId, Pageable pages);
	Page<Video> findAllByOrderByPublicadoDesc(Pageable pages);
	Page<Video> findAllByUserIdOrderByPublicadoDesc(String userId, Pageable pages);
	Page<Video> findAllByUserIdOrderByVisualizacoesDesc(String userId, Pageable pages);
	Page<Video> findAllByOrderByVisualizacoesDesc(Pageable pages);
}
