package com.andre.OverGame.api.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.andre.OverGame.api.entity.Video;

@Component
public interface VideoService {
	Optional<Video> findById(String id);
	Page<Video> findByTitle(String title, int page, int count);
	Page<Video> findByTitleOrUserName(String titleOrUsername, int page, int count);
	Page<Video> getAllVideosPaged(int count, int page);
	void deleteVideoById(String id);
	Video createOrUpdateVideo(Video video);
	Video encapsulaVideo(String caminho, String title, String description, String jogo);
	Page<Video> findAll(int page, int count);
	Page<Video> findAllOrderByDate(int page, int count);
	Page<Video> findVideoByUserIdOrderByDate(String userId,int page, int count);
	Page<Video> findVideoByUserIdOrderByVisualizacoes(String userId,int page, int count);
	Page<Video> findAllByMostViews(int page, int count);
}
