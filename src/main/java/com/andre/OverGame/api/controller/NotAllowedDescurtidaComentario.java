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

import com.andre.OverGame.api.entity.Comentarios;
import com.andre.OverGame.api.entity.CurtidaComentario;
import com.andre.OverGame.api.entity.DescurtidaComentario;
import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.service.ComentariosService;
import com.andre.OverGame.api.service.CurtidaComentarioService;
import com.andre.OverGame.api.service.DescurtidaComentarioService;
import com.andre.OverGame.api.utilities.UserUtilities;
import com.andre.OverGame.response.Response;

@RestController
@RequestMapping("/api/unauthorized/descurtidaComentario")
@CrossOrigin(origins="*")
public class NotAllowedDescurtidaComentario {

	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private CurtidaComentarioService curtidaComentarioService;
	
	@Autowired
	private DescurtidaComentarioService descurtidaComentarioService;
	
	@Autowired
	private ComentariosService comentariosService;
	
	
	@PostMapping
	public ResponseEntity<Response<DescurtidaComentario>> createDescurtida(HttpServletRequest request, @RequestBody DescurtidaComentario descurtida, BindingResult result){
		
		Response<DescurtidaComentario> response = new Response<DescurtidaComentario>();
		
		try {
			validateDesurtida(descurtida, result);
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Comentarios> comentariosOptional = this.comentariosService.findById(descurtida.getComentario().getId());
			Comentarios comentarios = comentariosOptional.get();
			User userLogado = this.userUtilities.userFromRequest(request);
			
			DescurtidaComentario descurtidaExiste = verifyDislikeExisting(userLogado.getId(), comentarios.getId());
			
			if(descurtidaExiste != null) {
				response.getErros().add("Você já descurtiu isso");
				return ResponseEntity.badRequest().body(response);
			}
			
			CurtidaComentario curtidaExiste = verifyLikeExisting(userLogado.getId(), comentarios.getId());
			
			if(curtidaExiste != null) {
				this.curtidaComentarioService.deleteCurtida(curtidaExiste.getId());
			}
			
			descurtida.setComentario(comentarios);
			descurtida.setUser(userLogado);
			
			DescurtidaComentario descurtidaPersisted = this.descurtidaComentarioService.createLike(descurtida);
			
			response.setData(descurtidaPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("comentário não encontrada para o comentário de id: "+descurtida.getComentario().getId());
			}else{
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
	private void validateDesurtida(DescurtidaComentario descurtida ,BindingResult result) {
		
		if(descurtida.getComentario() == null) {
			result.addError(new ObjectError("Descurtida", "Descurtida sem objeto de Comentário"));
		}else {
			if(descurtida.getComentario().getId() == null) {
				result.addError(new ObjectError("Descurtida", "Descurtida sem id de Comentário"));
			}
		}
	}
	
	
	private DescurtidaComentario verifyDislikeExisting(String userId, String comentarioId) {
		
		DescurtidaComentario descurtida = null;
		
		try {
			descurtida = this.descurtidaComentarioService.getDescurtidaComentarioByUserIdAndComentarioId(userId, comentarioId);
			
		}catch(Exception e) {
			descurtida = null;
		}
		return descurtida;
	}
	
	private CurtidaComentario verifyLikeExisting(String userId, String comentarioId) {
		CurtidaComentario curtida = null;
		
		try {
			
			curtida = this.curtidaComentarioService.getCurtidaComentarioByUserIdAndComentarioId(userId, comentarioId);
		}catch(Exception e) {
			curtida = null;
		}
		return curtida;
	}
	
	@GetMapping(value="{comentarioId}")
	public ResponseEntity<Response<DescurtidaComentario>> verifyLikeExist(HttpServletRequest request, @PathVariable String comentarioId){
		
		Response<DescurtidaComentario> response = new Response<DescurtidaComentario>();
		User userLogado = this.userUtilities.userFromRequest(request);
		DescurtidaComentario descurtida = verifyDislikeExisting(userLogado.getId(),comentarioId);
		
		response.setData(descurtida);
		
		return ResponseEntity.ok(response);
	}
	
	
	@DeleteMapping(value="{comentarioId}")
	public ResponseEntity<Response<String>> deleteCurtida(HttpServletRequest request, @PathVariable String comentarioId){
		
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			DescurtidaComentario descurtida = verifyDislikeExisting(userLogado.getId() ,comentarioId);
			if(descurtida == null) {
				response.getErros().add("você não curtiu isso");
				return ResponseEntity.badRequest().body(response);
			}
			
			this.descurtidaComentarioService.deleteDescurtida(descurtida.getId());
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return ResponseEntity.ok(response);
	}
}
