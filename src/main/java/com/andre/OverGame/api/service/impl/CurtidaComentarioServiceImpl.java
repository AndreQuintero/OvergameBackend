package com.andre.OverGame.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.CurtidaComentario;
import com.andre.OverGame.api.repository.CurtidaComentarioRepository;
import com.andre.OverGame.api.service.CurtidaComentarioService;

@Service
public class CurtidaComentarioServiceImpl implements CurtidaComentarioService{

	@Autowired
	private CurtidaComentarioRepository curtidaComentarioRepository;

	@Override
	public CurtidaComentario createLike(CurtidaComentario curtida) {
		
		return this.curtidaComentarioRepository.save(curtida) ;
	}

	@Override
	public void deleteCurtida(String id) {
		
		this.curtidaComentarioRepository.deleteById(id);
	}

	@Override
	public Integer getCountLikesOfACommentary(String comentarioId) {
		
		return this.curtidaComentarioRepository.countByComentarioId(comentarioId);
	}

	@Override
	public CurtidaComentario getCurtidaComentarioByUserIdAndComentarioId(String userId, String comentarioId) {
		
		return this.curtidaComentarioRepository.findByComentarioIdAndUserId(comentarioId, userId);
	}
	
	
	
}
