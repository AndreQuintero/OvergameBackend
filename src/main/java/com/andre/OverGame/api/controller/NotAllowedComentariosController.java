package com.andre.OverGame.api.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andre.OverGame.api.entity.Comentarios;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.entity.Video;
import com.andre.OverGame.api.enums.ProfileEnum;
import com.andre.OverGame.api.service.ComentariosService;
import com.andre.OverGame.api.service.VideoService;
import com.andre.OverGame.api.utilities.UserUtilities;
import com.andre.OverGame.response.Response;

@RestController
@CrossOrigin(value ="*")
@RequestMapping("/api/unauthorized/comentarios")
public class NotAllowedComentariosController {

	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private VideoService videoService;
	
	@Autowired
	private ComentariosService comentarioService;
	
	private void verifyComentarios(BindingResult result, Comentarios comentarios, Boolean createOrUpdate) {
		
		if(createOrUpdate == false) {
			if(comentarios.getId() == null) {
				result.addError(new ObjectError("Comentários", "Comentário sem id"));
			}
			
		}else {
		
			if(comentarios.getVideo() == null) {
				
				result.addError(new ObjectError("Comentarios", "Comentário sem objeto de vídeo"));
			}else {
				if(comentarios.getVideo().getId() == null) {
					result.addError(new ObjectError("Comentarios", "Comentário sem id de vídeo"));
				}
			}
			
			
		}
		
		if(comentarios.getMensagem() == null) {
			result.addError(new ObjectError("Comentários", "Comentário sem mensagem"));
		}
		
		
		
	}
	
	@PostMapping
	public ResponseEntity<Response<Comentarios>> postComentariosInAVideo(@RequestBody Comentarios comentarios, HttpServletRequest request, BindingResult result){
		
		Response<Comentarios> response = new Response<Comentarios>();
		try {
			
			verifyComentarios(result, comentarios, true);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
	
			Optional<Video> videoOptional = this.videoService.findById(comentarios.getVideo().getId());
			Video video = videoOptional.get();
			
			if(video == null) {
				response.getErros().add("video inexistente com o id: "+ comentarios.getVideo().getId());
				return ResponseEntity.badRequest().body(response) ;
			}
			
			
			User userLogado = this.userUtilities.userFromRequest(request);
			
			
			comentarios.setVideo(video);
			comentarios.setUsuarioComment(userLogado);
			comentarios.setDislike(0);
			comentarios.setLike(0);
			comentarios.setPublicado(new Date());
			comentarios.setEditado(null);
			
			Comentarios comentariosPersisted = this.comentarioService.createOrUpdate(comentarios);
			response.setData(comentariosPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Video inexistente");
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		// chamar os servicos destes dois e encontrar pelo id do requestbody
		// preencher e salvar
		
		return ResponseEntity.ok(response);
	}
	
	
	@PutMapping
	public ResponseEntity<Response<Comentarios>> updateComentarios(@RequestBody Comentarios comentarios, HttpServletRequest request, BindingResult result){
		
		Response<Comentarios> response = new Response<Comentarios>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			
			verifyComentarios(result, comentarios, false);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Comentarios> comentarioOptional = this.comentarioService.findById(comentarios.getId());
			Comentarios comentarioBuscado = comentarioOptional.get();
			
			if(userLogado.getProfile() != ProfileEnum.ROLE_ADMIN) {
				if(this.userUtilities.idsEquals(userLogado.getId(), comentarioBuscado.getUsuarioComment().getId()) == false) {
					response.getErros().add("Você não tem permissão para editar esse comentário");
					return ResponseEntity.badRequest().body(response);
				}
			}
			
		    comentarioBuscado.setMensagem(comentarios.getMensagem());
			comentarioBuscado.setEditado(new Date());
		    
			Comentarios comentarioPersisted = this.comentarioService.createOrUpdate(comentarioBuscado);
			
			response.setData(comentarioPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Comentário inexistente");
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Response<String>> deleteComentario(@PathVariable String id, HttpServletRequest request){
		
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			
			// verifica se existe o comentário 
			
			Optional<Comentarios> comentarioOptional = this.comentarioService.findById(id);
			Comentarios comentario = comentarioOptional.get();
			
			if(userLogado.getProfile() != ProfileEnum.ROLE_ADMIN) {
				if(this.userUtilities.idsEquals(userLogado.getId(), comentario.getUsuarioComment().getId()) == false) {
					response.getErros().add("Você não tem permissão de excluir este comentário!");
					return ResponseEntity.badRequest().body(response);
				}
			}
			
			this.comentarioService.deleteComentarioById(id);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("Comentário inexistente");
			}else {
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
}
