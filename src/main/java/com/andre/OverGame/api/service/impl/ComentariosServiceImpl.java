package com.andre.OverGame.api.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.Comentarios;
import com.andre.OverGame.api.repository.ComentariosRepository;
import com.andre.OverGame.api.service.ComentariosService;

@Service
public class ComentariosServiceImpl implements ComentariosService{

	@Autowired
	private ComentariosRepository comentariosRepository;
	
	@Override
	public void deleteComentarioById(String id) {
		
		this.comentariosRepository.deleteById(id);
		
	}

	@Override
	public Page<Comentarios> findComentariosByVideoId(String videoId ,int page, int count) {
	    
		Pageable pages = PageRequest.of(page, count);
		return this.comentariosRepository.findByVideoIdOrderByPublicadoDesc(videoId, pages);
	}

	@Override
	public Comentarios createOrUpdate(Comentarios comentario) {
		// TODO Auto-generated method stub
		return this.comentariosRepository.save(comentario);
	}

	@Override
	public Optional<Comentarios> findById(String id) {
		
		return this.comentariosRepository.findById(id);
	}

}
