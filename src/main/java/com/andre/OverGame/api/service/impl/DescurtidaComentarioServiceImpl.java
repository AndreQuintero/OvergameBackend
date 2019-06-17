package com.andre.OverGame.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.DescurtidaComentario;
import com.andre.OverGame.api.repository.DescurtidaComentarioRepository;
import com.andre.OverGame.api.service.DescurtidaComentarioService;

@Service
public class DescurtidaComentarioServiceImpl implements DescurtidaComentarioService{

	@Autowired
	private DescurtidaComentarioRepository descurtidaComentarioRepository;
	
	@Override
	public DescurtidaComentario createLike(DescurtidaComentario descurtida) {
		
		return this.descurtidaComentarioRepository.save(descurtida);
	}

	@Override
	public void deleteDescurtida(String id) {
		
		this.descurtidaComentarioRepository.deleteById(id);
	}

	@Override
	public Integer getCountDislikesOfACommentary(String comentarioId) {
		
		return this.descurtidaComentarioRepository.countByComentarioId(comentarioId);
	}

	@Override
	public DescurtidaComentario getDescurtidaComentarioByUserIdAndComentarioId(String userId, String comentarioId) {
		
		return this.descurtidaComentarioRepository.findByComentarioIdAndUserId(comentarioId, userId);
	}

}
