package com.clloret.days.domain.tags.order;

import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.tags.order.TagSortFactory.SortType;
import java.util.Comparator;
import org.junit.Test;

public class TagSortFactoryTest {

  private Tag tag1 = new Tag(null, "A");
  private Tag tag2 = new Tag(null, "Z");

  @Test
  public void makeTagSort_WhenSortTypeName_ReturnCorrectComparator() {

    Comparator<TagSortable> sut = TagSortFactory.makeTagSort(SortType.NAME);

    int result = sut.compare(tag1, tag2);

    assertThat(result, lessThan(0));
  }

}