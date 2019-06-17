package com.andre.OverGame;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andre.OverGame.api.entity.User;
import com.andre.OverGame.api.enums.ProfileEnum;
import com.andre.OverGame.api.repository.UserRepository;

@SpringBootApplication
public class OverGameApplication {

	public static void main(String[] args) {
		SpringApplication.run(OverGameApplication.class, args);
	}

	@Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            initUsers(userRepository, passwordEncoder);
        };

    }
	
	private void initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        User admin = new User();
        admin.setEmail("andre.quintero96@gmail.com");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setProfile(ProfileEnum.ROLE_ADMIN);
        admin.setDescription("Sou o dono do sistema.");
        admin.setAvatar("");
        admin.setCreatedBy(new Date());
        admin.setDataNascimento(new Date());

        User find = userRepository.findByEmail("andre.quintero96@gmail.com");
        if (find == null) {
            userRepository.save(admin);
        }
    }
}

