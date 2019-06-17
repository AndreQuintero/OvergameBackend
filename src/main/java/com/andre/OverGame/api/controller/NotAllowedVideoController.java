package com.andre.OverGame.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.andre.OverGame.api.entity.CurtidaVideo;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.entity.Video;
import com.andre.OverGame.api.enums.ProfileEnum;
import com.andre.OverGame.api.service.CurtidaVideoService;
import com.andre.OverGame.api.service.DescurtidaVideoService;
import com.andre.OverGame.api.service.VideoService;
import com.andre.OverGame.api.utilities.UserUtilities;
import com.andre.OverGame.api.utilities.VideoUtilities;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/unauthorized/video")
@CrossOrigin(origins="*")
public class NotAllowedVideoController {

	@Autowired
	private VideoService videoService;
	
	@Autowired
	private VideoUtilities videoUtilities;
	
	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private CurtidaVideoService curtidaVideoService;
	
	@Autowired
	private DescurtidaVideoService descurtidaVideoService;
	
	@PostMapping
	public ResponseEntity<Response<Video>> uploadVideo(@RequestParam MultipartFile video,  @RequestParam String titulo, @RequestParam String descricao,@RequestParam String jogo ,HttpServletRequest request){
		
		Response<Video> response = new Response<Video>();
		
		User userLogado = this.userUtilities.userFromRequest(request);
		userLogado.setPassword(null);
		
		String caminho = this.videoUtilities.salvarFoto(video);
		
		Video videoObj = this.videoService.encapsulaVideo(caminho, titulo, descricao, jogo);
		
		videoObj.setUser(userLogado);
		
		List<String> result = new ArrayList<String>();
		
		try {
			
			validateResult(result, videoObj);
			
			if(result.size() > 0) {
				
				response.setErros(result);
				return ResponseEntity.badRequest().body(response);
			}
			
			Video videoPersisted = this.videoService.createOrUpdateVideo(videoObj);
			
			response.setData(videoPersisted);
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return  ResponseEntity.ok(response);
	}
	
	private void validateResult (List<String> result, Video video) {
		
		if(video.getTitulo() == null) {
			result.add("O vídeo não possui título");
		}
		
		if(video.getDescricao() == null) {
			result.add("O vídeo não possui descrição");
		}
		
		if(video.getNomeJogo() == null) {
			result.add("O vídeo não possui o nome do jogo");
		}
	}
	
	@PutMapping
	public ResponseEntity<Response<Video>> updateVideo(@RequestBody Video video, HttpServletRequest request){

		Response<Video> response = new Response<Video>();
		List<String> erros = new ArrayList<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		
		
		
		try {
			validateResultUp(erros, video);
			
			if(erros.size() > 0) {
				response.setErros(erros);
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Video> videoOptional = this.videoService.findById(video.getId());
			
			Video videoExistente = videoOptional.get();
			
			if(videoExistente.getId() == null) {
				response.getErros().add("Não foi encontrado um video com o id: "+ video.getId());
				return ResponseEntity.badRequest().body(response);
			}
			
			if(userLogado.getProfile() != ProfileEnum.ROLE_ADMIN) {
				if(this.userUtilities.idsEquals(userLogado.getId(), videoExistente.getUser().getId()) == false) {
					response.getErros().add("Você não tem permissão de Alterar esse vídeo");
					return ResponseEntity.badRequest().body(response);
				}
			}
			
			
			
			videoExistente.setTitulo(video.getTitulo());
			videoExistente.setDescricao(video.getDescricao());
			videoExistente.setNomeJogo(video.getNomeJogo());
			Video videoPersisted = this.videoService.createOrUpdateVideo(videoExistente);
			videoPersisted.getUser().setPassword(null);
			
			response.setData(videoPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Não há vídeos para este id");
			}else {
				response.getErros().add(e.getMessage());
			}
			
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	
	private void validateResultUp (List<String> result, Video video) {
		
		if(video.getId() == null) {
			result.add("O vídeo não possui id");
		}
		
		if(video.getTitulo() == null) {
			result.add("O vídeo não possui título");
		}
		
		if(video.getDescricao() == null) {
			result.add("O vídeo não possui descrição");
		}
		if(video.getNomeJogo() == null) {
			result.add("O vídeo não possui o nome do jogo");
		}
	}
	
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Response<String>> deleteVideoById(@PathVariable String id, HttpServletRequest request){
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		
		try {
			Optional<Video> videoOptional = this.videoService.findById(id);
			Video video = videoOptional.get();
			
			if(userLogado.getProfile() != ProfileEnum.ROLE_ADMIN) {
				if(this.userUtilities.idsEquals(userLogado.getId(), video.getUser().getId()) == false) {
					response.getErros().add("Você não tem permissão de excluir esse vídeo");
					return ResponseEntity.badRequest().body(response);
				}
			}
			
			this.videoService.deleteVideoById(id);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Não foi encontrado um video com o id: "+ id);			
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping(value="LikedVideo/{page}/{count}")
	public ResponseEntity<Response<Page<Video>>> getLikedVideos(@PathVariable int page, @PathVariable int count, HttpServletRequest request){
		Response<Page<Video>> response = new Response<Page<Video>>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			Page<CurtidaVideo> curtidas = this.curtidaVideoService.getCurtidasVideosByUserId(userLogado.getId(), page, count);
			List<Video> list = new ArrayList<Video>();
			for(CurtidaVideo c : curtidas.getContent()) {
				c.getVideo().getUser().setPassword(null);
				c.getVideo().setLikes(this.curtidaVideoService.getCountLikesOfAVideo(c.getVideo().getId()));
				c.getVideo().setDislikes(this.descurtidaVideoService.getCountDislikesOfAVideo(c.getVideo().getId()));
				list.add(c.getVideo());
			}
			
			
			Page<Video> pageVideo = new PageImpl<Video>(list, curtidas.getPageable(), curtidas.getTotalElements());
			
			response.setData(pageVideo);
			
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return  ResponseEntity.ok(response);
		
	}
}
