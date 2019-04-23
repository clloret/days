package com.clloret.days.domain.utils;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

// TODO: 23/04/2019 Override all other methods
public class SortedValueMap<K, V extends C, C> extends HashMap<K, V> {

  private final Comparator<C> comparator;
  private SortedCollection<V, C> sortedCollection;

  public SortedValueMap(Comparator<C> comparator) {

    this.comparator = comparator;
  }

  private void sortValues() {

    if (sortedCollection != null) {
      sortedCollection.sort();
    }
  }

  @Override
  public V put(K key, V value) {

    V result = super.put(key, value);

    sortValues();

    return result;
  }

  @Override
  public V remove(Object key) {

    V result = super.remove(key);

    sortValues();

    return result;
  }

  public Collection<V> sortedValues() {

    sortedCollection = new SortedCollection<>(this.values(), comparator);

    return sortedCollection;
  }

  public void refreshValues() {

    sortValues();
  }
}
