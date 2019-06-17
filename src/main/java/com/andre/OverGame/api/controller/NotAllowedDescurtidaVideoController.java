package com.andre.OverGame.api.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andre.OverGame.api.entity.CurtidaVideo;
import com.andre.OverGame.api.entity.DescurtidaVideo;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.entity.Video;
import com.andre.OverGame.api.service.CurtidaVideoService;
import com.andre.OverGame.api.service.DescurtidaVideoService;
import com.andre.OverGame.api.service.VideoService;
import com.andre.OverGame.api.utilities.UserUtilities;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/unauthorized/descurtidaVideo")
@CrossOrigin(origins="*")
public class NotAllowedDescurtidaVideoController {

	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private CurtidaVideoService curtidaVideoService;
	
	@Autowired 
	private VideoService videoService;
	
	@Autowired 
	private DescurtidaVideoService descurtidaVideoService;
	
	
	@PostMapping
	public ResponseEntity<Response<DescurtidaVideo>> createDescurtida(HttpServletRequest request, @RequestBody DescurtidaVideo descurtida, BindingResult result){
		
		Response<DescurtidaVideo> response = new Response<DescurtidaVideo>();
		User userLogado = this.userUtilities.userFromRequest(request);
		try {
			validateDescurtida(descurtida, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Video> videoOptional = verifyVideoExisting(descurtida.getVideo().getId());
			
			if(videoOptional == null) {
				response.getErros().add("Video inexistente do id: "+ descurtida.getVideo().getId());
				return ResponseEntity.badRequest().body(response);
			}
			
			Video video = videoOptional.get();
			DescurtidaVideo dislikeExiste = verifyDislikeExisting(userLogado.getId(), video.getId());
			
			if(dislikeExiste != null) {
				response.getErros().add("Você já descurtiu esse vídeo");
				return ResponseEntity.badRequest().body(response);
			}
			
			CurtidaVideo curtida = verifyLikeExisting(userLogado.getId(), video.getId());
			
			if(curtida != null) {
				this.curtidaVideoService.deleteCurtida(curtida.getId());
			}
			
			descurtida.setUser(userLogado);
			descurtida.setVideo(video);
			
			DescurtidaVideo descurtidaPersisted = this.descurtidaVideoService.createLike(descurtida);
			
			response.setData(descurtidaPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Video com id inexistente");
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateDescurtida(DescurtidaVideo descurtida, BindingResult result) {
		
		if(descurtida.getVideo() == null) {
			result.addError(new ObjectError("Descurtida", "Objeto de vídeo nulo"));
		}else {
			if(descurtida.getVideo().getId() == null) {
				result.addError(new ObjectError("Descurtida", "Id de vídeo nulo"));
			}
		}
			
		
	}
	
	private Optional<Video> verifyVideoExisting(String id){
		
		Optional<Video> video = null;
		
		try {
			video = this.videoService.findById(id);
		}catch(Exception e) {
			video = null;
		}
		return video;
	}
	
	private DescurtidaVideo verifyDislikeExisting(String userId, String videoId) {
		
		DescurtidaVideo descurtida = null;
		
		try {
			descurtida = this.descurtidaVideoService.getDescurtidaVideoByUserIdAndVideoId(userId, videoId);
			
		}catch(Exception e) {
			descurtida = null;
		}
		return descurtida;
	}
	
	private CurtidaVideo verifyLikeExisting(String userId, String videoId) {
		
		CurtidaVideo curtida = null;
		
		try {
			
			curtida = this.curtidaVideoService.getCurtidaVideoByUserIdAndVideoId(userId, videoId);
		}catch(Exception e) {
			
			curtida = null;
		}
		return curtida;
	}
	
	@GetMapping(value="{videoId}")
	public ResponseEntity<Response<DescurtidaVideo>> verifyDislikeExist(HttpServletRequest request, @PathVariable String videoId){
		
		Response<DescurtidaVideo> response = new Response<DescurtidaVideo>();
		User userLogado = this.userUtilities.userFromRequest(request);
		DescurtidaVideo descurtida = verifyDislikeExisting(userLogado.getId(), videoId);
		
		response.setData(descurtida);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value= "{videoId}")
		public ResponseEntity<Response<String>> deleteCurtida(HttpServletRequest request, @PathVariable String videoId){
		
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			DescurtidaVideo descurtida = verifyDislikeExisting(userLogado.getId(), videoId);
			if(descurtida == null) {
				response.getErros().add("você não descurtiu isso");
				return ResponseEntity.badRequest().body(response);
			}
			
			this.descurtidaVideoService.deleteDescurtida(descurtida.getId());
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return ResponseEntity.ok(response);
	}
	
}
