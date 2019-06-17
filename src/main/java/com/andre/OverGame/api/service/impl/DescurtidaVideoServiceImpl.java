package com.andre.OverGame.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.DescurtidaVideo;
import com.andre.OverGame.api.repository.DescurtidaVideoRepository;
import com.andre.OverGame.api.service.DescurtidaVideoService;


@Service
public class DescurtidaVideoServiceImpl implements DescurtidaVideoService{

	@Autowired
	private DescurtidaVideoRepository descurtidaVideoRepository;
	
	@Override
	public DescurtidaVideo createLike(DescurtidaVideo descurtida) {
		
		return this.descurtidaVideoRepository.save(descurtida);
	}

	@Override
	public void deleteDescurtida(String id) {
		
		this.descurtidaVideoRepository.deleteById(id);
		
	}

	@Override
	public Integer getCountDislikesOfAVideo(String videoId) {
	
		return this.descurtidaVideoRepository.countByVideoId(videoId);
	}

	@Override
	public DescurtidaVideo getDescurtidaVideoByUserIdAndVideoId(String userId, String videoId) {

		return this.descurtidaVideoRepository.findByVideoIdAndUserId(videoId, userId);
	}

}
