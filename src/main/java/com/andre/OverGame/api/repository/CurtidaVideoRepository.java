package com.andre.OverGame.api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.andre.OverGame.api.entity.CurtidaVideo;

public interface CurtidaVideoRepository extends MongoRepository<CurtidaVideo, String> {

	List<CurtidaVideo> findByVideoId(String videoId); // pra me retornar quantos registros existes
	CurtidaVideo findByVideoIdAndUserId(String videoId, String UserId); // pra ver se o usuário já curtiu o vídeo ou não
	Integer countByVideoId(String videoId);
	
	Page<CurtidaVideo> findByUserId(String userId,Pageable page);
}
