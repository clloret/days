package com.clloret.days.domain.utils;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;

public class StringUtilsTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  public void isNullOrEmpty_whenIsNull_ReturnTrue() {

    boolean result = StringUtils.isNullOrEmpty(null);

    assertThat(result).isTrue();
  }

  @Test
  public void isNullOrEmpty_whenIsEmpty_ReturnTrue() {

    boolean result = StringUtils.isNullOrEmpty("");

    assertThat(result).isTrue();
  }

  @Test
  public void isNullOrEmpty_whenNotIsEmpty_ReturnFalse() {

    boolean result = StringUtils.isNullOrEmpty("Not empty");

    assertThat(result).isFalse();
  }

}