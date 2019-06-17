package com.andre.OverGame.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andre.OverGame.api.entity.Comentarios;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.entity.Video;
import com.andre.OverGame.api.service.ComentariosService;
import com.andre.OverGame.api.service.CurtidaComentarioService;
import com.andre.OverGame.api.service.CurtidaVideoService;
import com.andre.OverGame.api.service.DescurtidaComentarioService;
import com.andre.OverGame.api.service.DescurtidaVideoService;
import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.api.service.VideoService;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/authorized/video")
@CrossOrigin(origins="*")
public class AllowedVideoController {

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ComentariosService comentariosService;
	
	@Autowired 
	private CurtidaVideoService curtidaVideoService;
	
	@Autowired
	private DescurtidaVideoService descurtidaVideoService;
	
	
	@Autowired
	private CurtidaComentarioService curtidaComentarioService;
	
	@Autowired
	private DescurtidaComentarioService descurtidaComentarioService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getVideos(@PathVariable int page, @PathVariable int count){
		
		Response<Page<Video>> response = new Response<Page<Video>>();
		
		try {
			
			 Page<Video> videos = this.videoService.findAllOrderByDate(page, count);
			    
				for(Video v : videos.getContent()) {
					v.setLikes(this.curtidaVideoService.getCountLikesOfAVideo(v.getId()));
					v.setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(v.getId()));
				}
				
			 response.setData(videos);
			 
			 
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	} 
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Response<Video>> findVideoById(@PathVariable String id){
		
		Response<Video> response = new Response<Video>();
		try {
			Optional<Video> videoOptional = this.videoService.findById(id);
			
			Video video = videoOptional.get(); 
			
			video.setVisualizacoes(video.getVisualizacoes() + 1);
			
			Page<Comentarios> comentariosPage = this.comentariosService.findComentariosByVideoId(video.getId(), 0, 5);
			List<Comentarios> list = comentariosPage.getContent();
			for(Comentarios c : list) {
				c.setDislike(this.descurtidaComentarioService.getCountDislikesOfACommentary(c.getId()));
				c.setLike(this.curtidaComentarioService.getCountLikesOfACommentary(c.getId()));
				User userComment = this.userService.findById(c.getUsuarioComment().getId()).get();
				c.setUsuarioComment(userComment);
				c.getUsuarioComment().setPassword(null);
				c.getVideo().getUser().setPassword(null);
				
			}
			
			Integer likes = this.curtidaVideoService.getCountLikesOfAVideo(id);
			Integer dislikes = this.descurtidaVideoService.getCountDislikesOfAVideo(id);
			
		
			Video videoPersisted = this.videoService.createOrUpdateVideo(video);
			videoPersisted.getUser().setPassword(null);
			videoPersisted.setComentarios(comentariosPage);
			videoPersisted.setLikes(likes);
			videoPersisted.setDislikes(dislikes);
			response.setData(videoPersisted);
			
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value= "{title}/{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getVideoByTitle(@PathVariable String title, @PathVariable int page, @PathVariable int count){
		
		Response<Page<Video>> response = new Response<Page<Video>>();
		
		try {
			
			Page<Video> videos = this.videoService.findByTitle(title, page, count);
			for(Video v : videos.getContent()) {
				v.getUser().setPassword(null);
				v.setLikes(this.curtidaVideoService.getCountLikesOfAVideo(v.getId()));
				v.setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(v.getId()));
			}
			response.setData(videos);
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("EmAlta/{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getMostVideosViews(@PathVariable int page, @PathVariable int count) {
		Response<Page<Video>> response = new Response<Page<Video>>();
		
		try {
			
			Page<Video> videos = this.videoService.findAllByMostViews(page, count);
			for(Video v : videos.getContent()) {
				v.getUser().setPassword(null);
				v.setLikes(this.curtidaVideoService.getCountLikesOfAVideo(v.getId()));
				v.setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(v.getId()));
			}
			response.setData(videos);
			System.out.println("chegou");
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping(value="userDate/{userId}/{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getUserIdMostRecentsVideos(@PathVariable String userId, @PathVariable int page, @PathVariable int count){
		
		Response<Page<Video>> response = new Response<Page<Video>>();
		
		try {
			
			Page<Video> videos = this.videoService.findVideoByUserIdOrderByDate(userId, page, count);
			for(Video v : videos.getContent()) {
				v.getUser().setPassword(null);
				v.setLikes(this.curtidaVideoService.getCountLikesOfAVideo(v.getId()));
				v.setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(v.getId()));
			}
			response.setData(videos);
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return  ResponseEntity.ok(response);
	}
	
	@GetMapping(value="userViews/{userId}/{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getUserIdMostViewsVideos(@PathVariable String userId, @PathVariable int page, @PathVariable int count){
		
		Response<Page<Video>> response = new Response<Page<Video>>();
		
		try {
			
			Page<Video> videos = this.videoService.findVideoByUserIdOrderByVisualizacoes(userId, page, count);
			for(Video v : videos.getContent()) {
				v.getUser().setPassword(null);
				v.setLikes(this.curtidaVideoService.getCountLikesOfAVideo(v.getId()));
				v.setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(v.getId()));
			}
			response.setData(videos);
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return  ResponseEntity.ok(response);
	}

}

