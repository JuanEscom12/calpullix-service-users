package com.calpullix.service.users.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.calpullix.service.users.model.RegisterUserRequest;
import com.calpullix.service.users.service.UserService;
import com.calpullix.service.users.util.AbstractWrapper;
import com.calpullix.service.users.util.ValidationHandler;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UsersHandler {

	@Value("${app.message-error-location-body}")
	private String messageErrorLocationBody;

	@Autowired
	private ValidationHandler validationHandler;
	
	@Autowired
	private UserService userService;

	@Timed(value = "calpullix.handler.users.metrics", description = "Restart password operation")
	public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
		log.info(":: Register User Handler {} ", serverRequest);
		return validationHandler.handle(input -> input.flatMap(request -> AbstractWrapper.async(() -> {
			log.info(":: Request {} ", request);
			return userService.saveRegister(request);
		})).flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response))), serverRequest,
				RegisterUserRequest.class).switchIfEmpty(Mono.error(new Exception(messageErrorLocationBody)));

	}

}
