package com.andre.OverGame.api.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.andre.OverGame.api.entity.Video;
import com.andre.OverGame.api.repository.VideoRepository;
import com.andre.OverGame.api.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {

	@Autowired
	private VideoRepository videoRepository; 
	
	@Override
	public Optional<Video> findById(String id) {
		
		return this.videoRepository.findById(id);
	}

	@Override
	public Page<Video> findByTitle(String title, int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findByTituloContainingIgnoreCaseOrderByPublicadoDescVisualizacoesDesc(title, pages);
	}

	/*@Override
	public Page<Video> findByTitleOrUserName(String titleOrUsername, int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findByTituloContainingIgnoreCaseOrUserUsernameIgnoreCase(titleOrUsername, titleOrUsername, pages);
	}*/

	@Override
	public Page<Video> getAllVideosPaged(int count, int page) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findAll(pages);
	}

	@Override
	public void deleteVideoById(String id) {
		
		this.videoRepository.deleteById(id);
	}

	@Override
	public Video createOrUpdateVideo(Video video) {
	
		return this.videoRepository.save(video);
	}

	@Override
	public Video encapsulaVideo(String caminho, String title, String description, String jogo) {
		Video video = new Video();
		
		video.setVideoCaminho(caminho);
		video.setTitulo(title);
		video.setDescricao(description);
		video.setNomeJogo(jogo);
		video.setDislikes(0);
		video.setLikes(0);
		video.setPublicado(new Date());
		video.setVisualizacoes(0);
		
		return video;
	}

	@Override
	public Page<Video> findAll(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findAll(pages);
	}

	@Override
	public Page<Video> findAllOrderByDate(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findAllByOrderByPublicadoDesc(pages);
	}

	@Override
	public Page<Video> findByTitleOrUserName(String titleOrUsername, int page, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Video> findVideoByUserIdOrderByDate(String userId, int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findAllByUserIdOrderByPublicadoDesc(userId, pages);
	}

	@Override
	public Page<Video> findVideoByUserIdOrderByVisualizacoes(String userId, int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return  this.videoRepository.findAllByUserIdOrderByVisualizacoesDesc(userId, pages);
	}

	@Override
	public Page<Video> findAllByMostViews(int page, int count) {
		Pageable pages = PageRequest.of(page, count);
		return this.videoRepository.findAllByOrderByVisualizacoesDesc(pages);
	}

	

}
