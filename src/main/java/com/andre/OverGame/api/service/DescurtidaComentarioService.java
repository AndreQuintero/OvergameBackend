package com.andre.OverGame.api.service;

import com.andre.OverGame.api.entity.DescurtidaComentario;

public interface DescurtidaComentarioService {

	DescurtidaComentario createLike(DescurtidaComentario descurtida);
	void deleteDescurtida(String id);
	Integer getCountDislikesOfACommentary(String comentarioId);
	DescurtidaComentario getDescurtidaComentarioByUserIdAndComentarioId(String userId, String comentarioId);
}
