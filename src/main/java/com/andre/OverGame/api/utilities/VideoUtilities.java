package com.andre.OverGame.api.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class VideoUtilities {
	
	@Value("${videos.diretorio.raiz}")
	private String raiz;
	
	@Value("${videos.diretorio.videos-upload}")
	private String diretorioVideos;
	
	@Value("${user.diretorio.imagens.usuario}")
	private String diretorioAvatares;
	
	public String salvarFoto(MultipartFile video) {
	
		return this.salvar(this.diretorioVideos, video);
	}
	
	public String salvarAvatar(MultipartFile avatar) {
		
		return this.salvar(this.diretorioAvatares, avatar);
	}
	
	public String salvar(String diretorio, MultipartFile video) {
		String numeroGerado = generateNumber();
		Path diretorioPath = Paths.get(this.raiz, diretorio);
		Path arquivoPath = diretorioPath.resolve(numeroGerado + video.getOriginalFilename());
		String path="";
		
		try {
			
			Files.createDirectories(diretorioPath);
			video.transferTo(arquivoPath.toFile());
		}catch(IOException e) {
			path = "ERROR";
			throw new RuntimeException(e.getMessage());
		}
		path = numeroGerado +video.getOriginalFilename();
		return path;
	}
	
	public  String generateNumber() {
		Random random = new Random();
		return Integer.toString(random.nextInt(999999));
	}
}
