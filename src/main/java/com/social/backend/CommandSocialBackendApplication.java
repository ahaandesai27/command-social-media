package com.social.backend;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class CommandSocialBackendApplication {

    private static final Logger log =
            LoggerFactory.getLogger(CommandSocialBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CommandSocialBackendApplication.class, args);
	}

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // dont map projects using model mapper
//        modelMapper.typeMap(UserDto.class, User.class)
//                .addMappings(mapper -> mapper.skip(User::setProjects));
//
        return modelMapper;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Application started successfully.");
    }
}
