package com.clloret.days.domain.utils;

import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressWarnings({"PMD.NullAssignment", "PMD.ShortMethodName"})
public class Optional<T> {

  private T value;

  private Optional() {

    this.value = null;
  }

  private Optional(T value) {

    this.value = Objects.requireNonNull(value);
  }

  public static <T> Optional<T> empty() {

    return new Optional<>();
  }

  public static <T> Optional<T> of(T value) {

    return new Optional<>(value);
  }

  public static <T> Optional<T> ofNullable(T value) {

    return value == null ? empty() : of(value);
  }

  public void ifPresent(Action<T> action) {

    if (value != null) {
      action.apply(value);
    }
  }

  public T get() {

    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  public boolean isPresent() {

    return value != null;
  }

  public interface Action<T> {

    void apply(T value);
  }
}