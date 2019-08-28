package com.clloret.days.domain.utils;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class StringUtilsTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  public void isNullOrEmpty_whenIsNull_ReturnTrue() {

    boolean result = StringUtils.isNullOrEmpty(null);

    assertThat(result, is(true));
  }

  @Test
  public void isNullOrEmpty_whenIsEmpty_ReturnTrue() {

    boolean result = StringUtils.isNullOrEmpty("");

    assertThat(result, is(true));
  }

  @Test
  public void isNullOrEmpty_whenNotIsEmpty_ReturnFalse() {

    boolean result = StringUtils.isNullOrEmpty("Not empty");

    assertThat(result, is(false));
  }

}