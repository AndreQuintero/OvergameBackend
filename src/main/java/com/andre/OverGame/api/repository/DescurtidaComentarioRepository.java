package com.andre.OverGame.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.andre.OverGame.api.entity.DescurtidaComentario;

public interface DescurtidaComentarioRepository extends MongoRepository<DescurtidaComentario, String> {

	List<DescurtidaComentario> findByComentarioId(String comentarioId); // pra me retornar quantos registros existes
	DescurtidaComentario findByComentarioIdAndUserId(String comentarioId, String UserId); // pra ver se o usuário já curtiu o vídeo ou não
	Integer countByComentarioId(String comentarioId);
}
