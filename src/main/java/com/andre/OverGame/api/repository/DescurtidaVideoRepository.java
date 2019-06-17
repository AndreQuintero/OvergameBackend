package com.andre.OverGame.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.andre.OverGame.api.entity.DescurtidaVideo;

public interface DescurtidaVideoRepository extends MongoRepository<DescurtidaVideo, String> {

	List<DescurtidaVideo> findByVideoId(String videoId); // pra me retornar quantos registros existes
	DescurtidaVideo findByVideoIdAndUserId(String videoId, String UserId); // pra ver se o usuário já curtiu o vídeo ou não
	Integer countByVideoId(String id);
}
