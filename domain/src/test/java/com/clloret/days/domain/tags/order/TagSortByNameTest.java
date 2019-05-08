package com.clloret.days.domain.tags.order;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

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

    assertThat(result, lessThan(0));
  }

  @Test
  public void compare_WhenFirstNameIsGreater_ReturnGreaterThanZero() {

    int result = sut.compare(tag2, tag1);

    assertThat(result, greaterThan(0));
  }

  @Test
  public void compare_whenNamesAreEquals_ReturnZero() {

    int result = sut.compare(tag1, tag1);

    assertThat(result, equalTo(0));
  }
}