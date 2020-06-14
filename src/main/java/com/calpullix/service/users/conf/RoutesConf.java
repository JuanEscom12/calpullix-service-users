package com.calpullix.service.users.conf;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.calpullix.service.users.handler.UsersHandler;

@Configuration
public class RoutesConf {

	@Value("${app.path-register-user}")
	private String pathRegisterUser;
	
	
	@Bean
	public RouterFunction<ServerResponse> routesLogin(UsersHandler usersHandler) {
		return route(POST(pathRegisterUser), usersHandler::registerUser);
	}
	
}
