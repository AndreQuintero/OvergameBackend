package com.andre.OverGame.api.service;

import com.andre.OverGame.api.entity.DescurtidaVideo;

public interface DescurtidaVideoService {
	
	
	DescurtidaVideo createLike(DescurtidaVideo descurtida);
	void deleteDescurtida(String id);
	Integer getCountDislikesOfAVideo(String videoId);
	DescurtidaVideo getDescurtidaVideoByUserIdAndVideoId(String userId, String videoId);
}
