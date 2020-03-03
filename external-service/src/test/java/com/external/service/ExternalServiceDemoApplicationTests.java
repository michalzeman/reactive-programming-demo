package com.external.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ExternalServiceDemoApplicationTests {

  @Test
  void contextLoads() {
    CustomConsumer<String> comp = value -> {
      tsss("tt");
      System.out.println(value);
    };

    comp.andThen(this::tsss)
        .andThen(this::tsss)
        .accept("");
  }

  void tsss(String arg) throws Throwable {
    System.out.println(arg);
  }

}
