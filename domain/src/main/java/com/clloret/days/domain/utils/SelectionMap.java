package com.clloret.days.domain.utils;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SelectionMap<K, V> extends LinkedHashMap<K, V> {

  protected transient Set<V> selection;

  public SelectionMap() {

    selection = newEmptySelection();
  }

  public SelectionMap(Map<? extends K, ? extends V> m) {

    super(m);

    selection = newEmptySelection();
  }

  protected Set<V> newEmptySelection() {

    return new LinkedHashSet<>();
  }

  public Set<V> getSelection() {

    return Collections.unmodifiableSet(selection);
  }

  public void setSelection(Collection<? extends V> selection) {

    if (isSelectionChanged(selection)) {
      this.selection.clear();
      this.selection.addAll(selection);
    }
  }

  private boolean isSelectionChanged(Collection<? extends V> selection) {

    if (this.selection.size() != selection.size()) {
      return true;
    }

    for (final V e : selection) {
      if (!this.selection.contains(e)) {
        return true;
      }
    }
    return false;
  }

  public boolean isSelected(V obj) {

    return !isSelectionEmpty() && (selection.size() == 1 ? Objects
        .equals(selection.iterator().next(), obj)
        : selection.contains(obj));
  }

  public boolean isSelectionEmpty() {

    return selection.isEmpty();
  }

  public boolean addToSelection(V obj) {

    return selection.add(obj);
  }

  public boolean removeFromSelection(V obj) {

    return selection.remove(obj);
  }

  public void clearSelection() {

    if (!selection.isEmpty()) {
      selection.clear();
    }
  }

  @NonNull
  public List<? extends K> getKeySelection(Function<? super V, ? extends K> mapper) {

    return Observable.just(selection)
        .concatMap(Observable::fromIterable)
        .map(mapper)
        .toList()
        .blockingGet();
  }

}