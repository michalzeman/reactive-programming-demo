package com.reactive.reactive.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Stream;

public class ClodVSHotStreamTest {

  Scheduler schedulerPublisher = Schedulers.newParallel("scheduler-publisher", 4);

  @Test
  public void coldTest() throws InterruptedException {

    Flux<Integer> cold = Flux.range(1, 7);

    cold.subscribe(i -> {
      System.out.println(String.format("1. subscribe - (%s), Thread: %s", i, Thread.currentThread().getName()));
    });

    Thread.sleep(3000);

    cold.subscribe(i -> {
      System.out.println(String.format("2. subscribe - (%s), Thread: %s", i, Thread.currentThread().getName()));
    });

  }

  @Test
  public void hotStreamSimTest() throws InterruptedException {

    Flux<Object> hotSource = Flux.create(sink ->
        Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).forEach(i -> {
          try {
            Thread.sleep(500);
            sink.next(i);
            if (i == 9) {
              sink.complete();
            }
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        })
    ).subscribeOn(schedulerPublisher)
        .share();

    hotSource.subscribe(i -> {
      System.out.println(String.format("1. subscribe - (%s), Thread: %s", i, Thread.currentThread().getName()));
    });

    // Wait for the second subscriber
    Thread.sleep(3000);

    hotSource.subscribe(i -> {
      System.out.println(String.format("2. subscribe - (%s), Thread: %s", i, Thread.currentThread().getName()));
    });

    // Wait until finish
    hotSource.blockLast();
  }

}
