package com.external.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@Configuration
public class ExternalServiceDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalServiceDemoApplication.class, args);
	}


	@Bean
	public RouterFunction<ServerResponse> statisticRoute(ExternalServiceHandler externalServiceHandler) {
		return RouterFunctions.route()
				.add(externalServiceHandler.route())
				.build();
	}
}
