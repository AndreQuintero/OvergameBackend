package com.andre.OverGame.api.service;

import com.andre.OverGame.api.entity.CurtidaComentario;

public interface CurtidaComentarioService {

	CurtidaComentario createLike(CurtidaComentario curtida);
	void deleteCurtida(String id);
	Integer getCountLikesOfACommentary(String comentarioId);
	CurtidaComentario getCurtidaComentarioByUserIdAndComentarioId(String userId, String comentarioId);
}
