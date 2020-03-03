package com.external.service;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface CustomConsumer<T> extends Consumer<T> {

  /**
   * Overridden method of ConsumerWithThrowable that will call acceptWithThrowable, but catching any exceptions.
   *
   * @param v1 parameter to overridden method
   */
  @Override
  default void accept(final T v1) {
    try {
      acceptWithThrowable(v1);
    } catch (final RuntimeException | Error exception) {
      throw exception;
    } catch (final Throwable throwable) {
      throw new RuntimeException(throwable);
    }
  }

  /**
   * Functional method that will throw exceptions.
   *
   * @param v1 parameter to overridden method
   * @throws E some exception
   */
  void acceptWithThrowable(final T v1) throws Throwable;

  default CustomConsumer<T> andThen(CustomConsumer<? super T> after) {
    Objects.requireNonNull(after);
    return (T t) -> {
      accept(t);
      after.accept(t);
    };
  }

}
