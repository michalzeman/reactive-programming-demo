package com.external.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class ExternalServiceHandler {

  public Mono<ServerResponse> getValue(ServerRequest request) {
    final var delay = Long.valueOf(request.pathVariable("delay"));
    return ServerResponse
        .ok()
        .contentType(MediaType.TEXT_PLAIN)
        .body(Mono.fromCallable(() -> "Value OK: "+delay).delayElement(Duration.ofMillis(delay)), String.class);
  }

  public RouterFunction<ServerResponse> route() {
    var route = RouterFunctions
        .route(GET("/{delay}").and(accept(MediaType.APPLICATION_JSON)), this::getValue);

    return RouterFunctions.route()
        .nest(path("/external"), () -> route)
        .build();
  }
}
