package com.andre.OverGame.api.controller;

import java.util.List;

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
import com.andre.OverGame.api.service.ComentariosService;
import com.andre.OverGame.api.service.CurtidaComentarioService;
import com.andre.OverGame.api.service.DescurtidaComentarioService;
import com.andre.OverGame.api.service.UserService;
import com.andre.OverGame.response.Response;

@RestController
@CrossOrigin(value ="*")
@RequestMapping("/api/authorized/comentarios")
public class AllowedComentariosController {

	@Autowired
	private ComentariosService comentarioService;
	
	@Autowired
	private CurtidaComentarioService curtidaComentarioService;
	
	@Autowired
	private DescurtidaComentarioService descurtidaComentarioService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value ="{videoId}/{page}")
	public ResponseEntity<Response<List<Comentarios>>> getCommentsOfAVideo(@PathVariable String videoId, @PathVariable int page){
		
		Response<List<Comentarios>> response = new Response<List<Comentarios>>();
		
		try {
			Page<Comentarios> comentariosPaged = this.comentarioService.findComentariosByVideoId(videoId, page, 5); 
			List<Comentarios> list = comentariosPaged.getContent();
			for(Comentarios c : list) {
				Integer likes = this.curtidaComentarioService.getCountLikesOfACommentary(c.getId());
				Integer dislikes = this.descurtidaComentarioService.getCountDislikesOfACommentary(c.getId());
				User userComment = this.userService.findById(c.getUsuarioComment().getId()).get();
				c.setUsuarioComment(userComment);
				c.getUsuarioComment().setPassword(null);
				c.getVideo().getUser().setPassword(null);
				c.setLike(likes);
				c.setDislike(dislikes);
			}
			
			response.setData(list);
			
			
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		
		return ResponseEntity.ok(response);
	}
	
}
