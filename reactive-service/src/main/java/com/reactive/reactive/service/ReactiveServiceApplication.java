package com.reactive.reactive.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@SpringBootApplication
@Configuration
public class ReactiveServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveServiceApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> statisticRoute(ReactiveServiceHandler reactiveServiceHandler) {
		return RouterFunctions.route()
				.add(reactiveServiceHandler.route())
				.build();
	}

}
