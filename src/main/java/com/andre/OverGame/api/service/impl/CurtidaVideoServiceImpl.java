package com.andre.OverGame.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.CurtidaVideo;
import com.andre.OverGame.api.repository.CurtidaVideoRepository;

@Service
public class CurtidaVideoServiceImpl implements com.andre.OverGame.api.service.CurtidaVideoService{

	@Autowired
	private CurtidaVideoRepository curtidaVideoRepository;
	
	@Override
	public CurtidaVideo createLike(CurtidaVideo curtida) {
		
		return this.curtidaVideoRepository.save(curtida);
	}

	@Override
	public void deleteCurtida(String id) {
		
		this.curtidaVideoRepository.deleteById(id);
	}

	@Override
	public Integer getCountLikesOfAVideo(String videoId) {
		
		return this.curtidaVideoRepository.countByVideoId(videoId);
	}

	@Override
	public CurtidaVideo getCurtidaVideoByUserIdAndVideoId(String userId, String videoId) {
		
		return this.curtidaVideoRepository.findByVideoIdAndUserId(videoId, userId);
	}

	@Override
	public Page<CurtidaVideo> getCurtidasVideosByUserId(String userId, int page, int count) {
		
		Pageable pages = PageRequest.of(page, count);
		return this.curtidaVideoRepository.findByUserId(userId, pages);
	}


	
	
}
