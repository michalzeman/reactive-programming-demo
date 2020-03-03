package com.reactive.reactive.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class ReactiveServiceHandler {

  private final WebClient externalServiceClient = WebClient.create("http://localhost:8083");

  public Mono<ServerResponse> readExternalResourceParallel(ServerRequest request) {
    final var delay = Long.valueOf(request.pathVariable("delay"));
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(composeExternalValuesParallel(delay), String.class);
  }

  private Flux<String> composeExternalValuesParallel(Long delay) {
    return Flux.zip(
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay)
    ).map(r -> String.format("Result of combination %s, %s, %s, %s, %s, %s,",
        r.getT1(),
        r.getT2(),
        r.getT3(),
        r.getT4(),
        r.getT5(),
        r.getT6())
    );
  }

  public Mono<ServerResponse> readExternalResourceSequence(ServerRequest request) {
    final var delay = Long.valueOf(request.pathVariable("delay"));
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(composeExternalValuesSequence(delay), String.class);
  }

  private Flux<String> composeExternalValuesSequence(Long delay) {
    return Flux.concat(
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay),
        callExtService(delay)
    );
  }

  private Mono<String> callExtService(Long delay) {
    return externalServiceClient
        .get()
        .uri("/external/{delay}", delay)
        .retrieve()
        .bodyToMono(String.class);
  }

  public RouterFunction<ServerResponse> route() {
    var route = RouterFunctions
        .route(GET("/parallel/{delay}").and(accept(MediaType.APPLICATION_JSON)), this::readExternalResourceParallel)
        .andRoute(GET("/sequence/{delay}").and(accept(MediaType.APPLICATION_JSON)), this::readExternalResourceSequence);

    return RouterFunctions.route()
        .nest(path("/reactive/external"), () -> route)
        .build();
  }
}
