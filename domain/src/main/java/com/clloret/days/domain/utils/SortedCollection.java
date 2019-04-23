package com.clloret.days.domain.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

class SortedCollection<V extends C, C> implements Collection<V> {

  private final Comparator<C> comparator;
  private final Collection<V> values;
  private Collection<V> sortedValues;

  SortedCollection(Collection<V> values, Comparator<C> comparator) {

    this.values = values;
    this.comparator = comparator;

    sort();
  }

  @Override
  public int size() {

    return values.size();
  }

  @Override
  public boolean isEmpty() {

    return values.isEmpty();
  }

  @Override
  public boolean contains(Object o) {

    return values.contains(o);
  }

  @NotNull
  @Override
  public Iterator<V> iterator() {

    return sortedValues.iterator();
  }

  @NotNull
  @Override
  public Object[] toArray() {

    return new Object[0];
  }

  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] a) {

    return sortedValues.toArray(a);
  }

  @Override
  public boolean add(V drawerTag) {

    return values.add(drawerTag);
  }

  @Override
  public boolean remove(Object o) {

    return values.remove(o);
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {

    return values.containsAll(c);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends V> c) {

    return values.addAll(c);
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {

    return values.removeAll(c);
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {

    return values.retainAll(c);
  }

  @Override
  public void clear() {

    values.clear();
  }

  void sort() {

    List<V> lstValues = new ArrayList<>(values);

    Collections.sort(lstValues, comparator);

    sortedValues = lstValues;
  }
}
