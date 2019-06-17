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
@RequestMapping("/api/unauthorized/curtidaVideo")
@CrossOrigin(origins="*")
public class NotAllowedCurtidaVideoController {

	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private CurtidaVideoService curtidaVideoService;
	
	@Autowired 
	private VideoService videoService;
	
	@Autowired 
	private DescurtidaVideoService descurtidaVideoService;
	
	@PostMapping
	public ResponseEntity<Response<CurtidaVideo>> createCurtida(HttpServletRequest request,@RequestBody CurtidaVideo curtida, BindingResult result){
		
		Response<CurtidaVideo> response = new Response<CurtidaVideo>();

		try {
			
			validateCurtida(curtida, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error-> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Video> videoOptional = this.videoService.findById(curtida.getVideo().getId());
			Video video = videoOptional.get();
			User userLogado = this.userUtilities.userFromRequest(request);
			
			
			
			// verifica se o usuário já curtiu o vídeo
			
			CurtidaVideo curtidaExiste = verifyLikeExisting(video.getId(), userLogado.getId());
			
			if(curtidaExiste != null) {
				response.getErros().add("Você já curtiu esse vídeo");
				return ResponseEntity.badRequest().body(response);
			}
			
			
			// verifica se ele havia dado dislike
			DescurtidaVideo descurtidaExiste = verifyDislikeExisting(video.getId(), userLogado.getId() );
			
			if(descurtidaExiste != null) {
				this.descurtidaVideoService.deleteDescurtida(descurtidaExiste.getId());
			}
			
			curtida.setUser(userLogado);
			curtida.setVideo(video);
			
			
			
			CurtidaVideo curtidaPersisted = this.curtidaVideoService.createLike(curtida);
			response.setData(curtidaPersisted);
			
		}catch(Exception e) {
			
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Video não encontrado pelo id: "+  curtida.getVideo().getId());
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateCurtida(CurtidaVideo curtida, BindingResult result) {
		
		if(curtida.getVideo() == null) {
			result.addError(new ObjectError("Curtida", "curtida sem objeto de vídeo"));
		}else {
			if(curtida.getVideo().getId() == null) {
				result.addError(new ObjectError("Curtida", "curtida sem id de vídeo"));
			}
		}
		
	}
	
	private CurtidaVideo verifyLikeExisting(String videoId, String userId) {
		
		CurtidaVideo curtida = null;
		try {
			curtida = this.curtidaVideoService.getCurtidaVideoByUserIdAndVideoId(userId, videoId);
		}catch(Exception e) {
			curtida = null;
		}
		
		return curtida;
	}
	
	private DescurtidaVideo verifyDislikeExisting(String videoId, String userId) {
		
		DescurtidaVideo descurtida = null;
		
		try {
			
			descurtida = this.descurtidaVideoService.getDescurtidaVideoByUserIdAndVideoId(userId, videoId);
			
		}catch(Exception e) {
			descurtida = null;
		}
		
		return descurtida;
	}
	
	@GetMapping(value="{videoId}")
	public ResponseEntity<Response<CurtidaVideo>> verifyLikeExist(HttpServletRequest request, @PathVariable String videoId){
		
		Response<CurtidaVideo> response = new Response<CurtidaVideo>();
		User userLogado = this.userUtilities.userFromRequest(request);
		CurtidaVideo curtida = verifyLikeExisting(videoId, userLogado.getId());
		
		response.setData(curtida);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "{videoId}")
	public ResponseEntity<Response<String>> deleteCurtida(HttpServletRequest request, @PathVariable String videoId){
		
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			CurtidaVideo curtida = verifyLikeExisting(videoId, userLogado.getId());
			if(curtida == null) {
				response.getErros().add("você não curtiu isso");
				return ResponseEntity.badRequest().body(response);
			}
			
			this.curtidaVideoService.deleteCurtida(curtida.getId());
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return ResponseEntity.ok(response);
	}
}
