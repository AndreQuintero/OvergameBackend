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
@RequestMapping("/api/unauthorized/curtidaComentario")
@CrossOrigin(origins="*")
public class NotAllowedCurtidaComentarioController {

	@Autowired
	private UserUtilities userUtilities;
	
	@Autowired
	private CurtidaComentarioService curtidaComentarioService;
	
	@Autowired
	private DescurtidaComentarioService descurtidaComentarioService;
	
	@Autowired
	private ComentariosService comentariosService;
	
	
	@PostMapping
	public ResponseEntity<Response<CurtidaComentario>> createCurtida(HttpServletRequest request, @RequestBody CurtidaComentario curtida, BindingResult result){
		
		Response<CurtidaComentario> response = new Response<CurtidaComentario>();
		
		try {
			
			validateCurtida(curtida, result);
			
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			
			Optional<Comentarios> comentarioOptional = this.comentariosService.findById(curtida.getComentario().getId());
			Comentarios comentario = comentarioOptional.get();
			
			User userLogado = this.userUtilities.userFromRequest(request);
			
			
			CurtidaComentario curtidaExiste = verifyLikeExisting(userLogado.getId(), comentario.getId());
			
			if(curtidaExiste != null) {
				response.getErros().add("Você já curtiu esse comentário");
				return ResponseEntity.badRequest().body(response);
			}
			
			DescurtidaComentario descurtidaExiste = verifyDislikeExisting(userLogado.getId(), comentario.getId());
			
			if(descurtidaExiste != null) {
				this.descurtidaComentarioService.deleteDescurtida(descurtidaExiste.getId());
			}
	
			curtida.setComentario(comentario);
			curtida.setUser(userLogado);
			
			CurtidaComentario curtidaPersisted = this.curtidaComentarioService.createLike(curtida);
			
			response.setData(curtidaPersisted);
			
		}catch(Exception e) {
			if(e.getMessage().equals("No value present")) {
				response.getErros().add("comentário não encontrada para o comentário de id: "+curtida.getComentario().getId());
			}else{
				response.getErros().add(e.getMessage());
			}
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
		
	}
	
	
	// verifica se ele já tinha dado dislike naquele comentário
	private DescurtidaComentario verifyDislikeExisting(String userId, String comentarioId) {
		DescurtidaComentario descurtida = null;
		
		try {
			
			descurtida = this.descurtidaComentarioService.getDescurtidaComentarioByUserIdAndComentarioId(userId, comentarioId);
			
		}catch(Exception e) {
			descurtida = null;
		}
		return descurtida;
	}
	
	// verifica se o usuário já curtiu aquele comentário
	
	private CurtidaComentario verifyLikeExisting(String userId, String comentarioId) {
		
		CurtidaComentario curtida = null;
		
		try {
			curtida = this.curtidaComentarioService.getCurtidaComentarioByUserIdAndComentarioId(userId, comentarioId);
		}catch(Exception e) {
			curtida = null;
		}
		return curtida;
	}
	
	private void validateCurtida(CurtidaComentario curtida, BindingResult result) {
		
		if(curtida.getComentario() == null) {
			result.addError(new ObjectError("Curtida Comentario", "Curtida sem objeto de comentario"));
		}else {
			if(curtida.getComentario().getId() == null) {
				result.addError(new ObjectError("Curtida Comentario", "Curtida sem id de comentario"));
			}
		}
	
	}
	
	@GetMapping(value="{comentarioId}")
	public ResponseEntity<Response<CurtidaComentario>> verifyLikeExist(HttpServletRequest request, @PathVariable String comentarioId){
		
		Response<CurtidaComentario> response = new Response<CurtidaComentario>();
		User userLogado = this.userUtilities.userFromRequest(request);
		CurtidaComentario curtida = verifyLikeExisting(userLogado.getId(),comentarioId);
		
		response.setData(curtida);
		
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value="{comentarioId}")
	public ResponseEntity<Response<String>> deleteCurtida(HttpServletRequest request, @PathVariable String comentarioId){
		
		Response<String> response = new Response<String>();
		User userLogado = this.userUtilities.userFromRequest(request);
		
		try {
			CurtidaComentario curtida = verifyLikeExisting(userLogado.getId() ,comentarioId);
			if(curtida == null) {
				response.getErros().add("você não curtiu isso");
				return ResponseEntity.badRequest().body(response);
			}
			
			this.curtidaComentarioService.deleteCurtida(curtida.getId());
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		
		return ResponseEntity.ok(response);
	}
}
