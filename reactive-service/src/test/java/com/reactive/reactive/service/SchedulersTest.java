package com.reactive.reactive.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class SchedulersTest {

  Scheduler schedulerPublisher = Schedulers.newParallel("scheduler-publisher", 2);
  Scheduler schedulerPublisher2 = Schedulers.newParallel("scheduler-publisher2", 2);
  Scheduler schedulerSubscriber = Schedulers.newParallel("scheduler-subscriber", 2);
  Scheduler schedulerSubscriber2 = Schedulers.newParallel("scheduler-subscriber2", 2);

  @Test
  public void publishOnTest() {

    Flux.range(1, 2)
        .map(i -> {
          System.out.println(String.format("First map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .publishOn(schedulerPublisher)
        .map(i -> {
          System.out.println(String.format("Second map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribe();
  }

  @Test
  public void publishOnMultipleTest() {

    Flux.range(1, 2)
        .map(i -> {
          System.out.println(String.format("1. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .publishOn(schedulerPublisher)
        .map(i -> {
          System.out.println(String.format("2. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .publishOn(schedulerPublisher2)
        .map(i -> {
          System.out.println(String.format("3. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribe();
  }

  @Test
  public void subscribeOnTest() {

    Flux.range(1, 2)
        .map(i -> {
          System.out.println(String.format("First map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribeOn(schedulerSubscriber)
        .map(i -> {
          System.out.println(String.format("Second map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribe();
  }

  @Test
  public void subscribeOnTwoTest() {

    Flux.range(1, 2)
        .map(i -> {
          System.out.println(String.format("1. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribeOn(schedulerSubscriber)
        .map(i -> {
          System.out.println(String.format("2. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribeOn(schedulerSubscriber2)
        .map(i -> {
          System.out.println(String.format("3. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribe();
  }

  @Test
  public void flatMapTest() {

    Flux.range(1, 2)
        .map(i -> {
          System.out.println(String.format("1. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribeOn(schedulerSubscriber)
        .flatMap(i -> {
          System.out.println(String.format("2. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return Flux.range(1, 2)
              .map(j -> {
                System.out.println(String.format("flatMap - (%s), Thread: %s", j, Thread.currentThread().getName()));
                return j;
              });
        })
        .publishOn(schedulerPublisher)
        .map(i -> {
          System.out.println(String.format("3. map - (%s), Thread: %s", i, Thread.currentThread().getName()));
          return i;
        })
        .subscribe();
  }

}
