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

  @Test
  public void normalizeText_WhenSpecialsChars_ReturnOk() {

    String text = "Filtered áéíóúàèìòù";
    String result = StringUtils.normalizeText(text);

    assertThat(result).isEqualTo("filtered aeiouaeiou");
  }

  @Test
  public void capitalizeFirstLetter_Always_CapitalizeFirstLetter() {

    String text = "lowercase text";
    String result = StringUtils.capitalizeFirstLetter(text);

    assertThat(result).isEqualTo("Lowercase text");
  }

  @Test
  public void tryParseInt_Always_ReturnInt() {

    Integer result = StringUtils.tryParseInt("15");

    assertThat(result).isEqualTo(15);
  }

  @Test
  public void tryParseInt_WhenNotInt_ReturnNull() {

    Integer result = StringUtils.tryParseInt("XX");

    assertThat(result).isNull();
  }

}