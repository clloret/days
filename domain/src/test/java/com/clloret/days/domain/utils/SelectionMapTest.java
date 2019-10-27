package com.clloret.days.domain.utils;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class SelectionMapTest {

  private SelectionMap<String, String> sut;

  private void addSampleValues(Map<String, String> map) {

    map.put("key1", "value1");
    map.put("key2", "value2");
    map.put("key3", "value3");
  }

  private void addSampleValues() {

    addSampleValues(sut);
  }

  private void selectSampleValues() {

    List<String> selection = Arrays.asList("value1", "value2");
    sut.setSelection(selection);
  }

  @Before
  public void setUp() {

    sut = new SelectionMap<>();
  }

  @Test
  public void create_WhenInitWithMap_ReturnCorrectValues() {

    Map<String, String> map = new HashMap<>();
    addSampleValues(map);

    sut = new SelectionMap<>(map);

    assertThat(sut.values()).containsExactly("value1", "value2", "value3");
  }

  @Test
  public void newEmptySelection_Always_ReturnNotNullValue() {

    assertThat(sut.newEmptySelection()).isNotNull();
  }

  @Test
  public void getSelection_WhenHasSelection_ReturnNotEmptySet() {

    addSampleValues();

    selectSampleValues();

    Set<String> selection = sut.getSelection();

    assertThat(selection.isEmpty()).isNotEqualTo(true);
  }

  @Test
  public void setSelection_Always_SelectValues() {

    addSampleValues();

    List<String> selection1 = Arrays.asList("value1", "value2");
    List<String> selection2 = Arrays.asList("value1", "value3");

    sut.setSelection(selection1);
    sut.setSelection(selection2);

    assertThat(sut.isSelected("value3")).isTrue();
  }

  @Test
  public void isSelected_WhenValueIsSelected_ReturnTrue() {

    addSampleValues();

    selectSampleValues();

    assertThat(sut.isSelected("value1")).isTrue();
  }

  @Test
  public void isSelectionEmpty_WhenSelectionIsEmpty_ReturnTrue() {

    assertThat(sut.isSelectionEmpty()).isTrue();
  }

  @Test
  public void addToSelection_Always_SelectValue() {

    addSampleValues();

    sut.addToSelection("value1");

    assertThat(sut.isSelected("value1")).isTrue();
  }

  @Test
  public void removeFromSelection_Always_UnselectValue() {

    addSampleValues();

    selectSampleValues();

    sut.removeFromSelection("value1");

    boolean result = sut.isSelected("value1");

    assertThat(result).isFalse();
  }

  @Test
  public void clearSelection_Always_ClearSelectedValues() {

    addSampleValues();

    selectSampleValues();

    sut.clearSelection();

    assertThat(sut.isSelectionEmpty()).isTrue();
  }

  @Test
  public void getKeySelection_Always_ReturnKeySelectionValues() {

    addSampleValues();

    selectSampleValues();

    List<? extends String> keySelection = sut.getKeySelection(String::toUpperCase);

    assertThat(keySelection).hasSize(2);
    assertThat(keySelection.get(0)).isEqualTo("VALUE1");
  }
}