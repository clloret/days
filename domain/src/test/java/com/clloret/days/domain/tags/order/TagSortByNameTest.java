package com.clloret.days.domain.tags.order;

import static com.google.common.truth.Truth.assertThat;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.tags.order.TagSortStrategy.OrderType;
import org.junit.Test;

public class TagSortByNameTest {

  private Tag tag1 = new Tag(null, "A");
  private Tag tag2 = new Tag(null, "Z");

  private TagSortByName sut = new TagSortByName(OrderType.ASC);

  @Test
  public void compare_WhenFirstNameIsLower_ReturnLessThanZero() {

    int result = sut.compare(tag1, tag2);

    assertThat(result).isLessThan(0);
  }

  @Test
  public void compare_WhenFirstNameIsGreater_ReturnGreaterThanZero() {

    int result = sut.compare(tag2, tag1);

    assertThat(result).isGreaterThan(0);
  }

  @Test
  public void compare_WhenNamesAreEquals_ReturnZero() {

    int result = sut.compare(tag1, tag1);

    assertThat(result).isEqualTo(0);
  }
}