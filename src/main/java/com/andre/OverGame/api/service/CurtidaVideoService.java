package com.andre.OverGame.api.service;

import org.springframework.data.domain.Page;

import com.andre.OverGame.api.entity.CurtidaVideo;


public interface CurtidaVideoService {

	CurtidaVideo createLike(CurtidaVideo curtida);
	void deleteCurtida(String id);
	Integer getCountLikesOfAVideo(String videoId);
	CurtidaVideo getCurtidaVideoByUserIdAndVideoId(String userId, String videoId);
	Page<CurtidaVideo> getCurtidasVideosByUserId(String userId, int page, int count);
}
