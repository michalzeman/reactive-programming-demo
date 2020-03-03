package com.imperative.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/imperative/external")
public class ImperativeServiceController {

  public static final String ERROR = "Error";
  private final RestTemplate restTemplate = new RestTemplate();

  private final Executor executor = Executors.newFixedThreadPool(20);

  String externalServiceUrl = "http://localhost:8083/external/%s";

  @GetMapping(value = "/sequence/{delay}")
  List<String> readExternalResourceSequence(@PathVariable("delay") Long delay) {
    return Stream.of(delay, delay, delay, delay, delay, delay)
        .map(d -> String.format(externalServiceUrl, d))
        .map(url -> restTemplate.getForEntity(url, String.class))
        .map(ResponseEntity::getBody)
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/parallel/{delay}")
  List<String> readExternalResourceParallel(@PathVariable("delay") Long delay) {
    List<CompletableFuture<ResponseEntity<String>>> executions = Stream.of(delay, delay, delay, delay, delay, delay)
        .map(d -> String.format(externalServiceUrl, d))
        .map(url -> CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, String.class), executor)).collect(Collectors.toList());

    CompletableFuture<ResponseEntity<String>>[] futures = executions.toArray(CompletableFuture[]::new);

    CompletableFuture.allOf(futures);

    return executions.stream()
        .map(f -> {
          try {
            return f.get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
          }
          return ResponseEntity.ok(ERROR);
        })
        .map(ResponseEntity::getBody)
        .collect(Collectors.toList());
  }


  private CompletableFuture<ResponseEntity<String>> executeRemoveCall(Integer delay) {
    return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(String.format(externalServiceUrl, delay), String.class), executor);
  }

}
