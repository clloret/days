package com.clloret.days.events.list;

import androidx.appcompat.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

public class RxSearchObservable {

  public static Observable<String> fromView(SearchView searchView) {

    final PublishSubject<String> subject = PublishSubject.create();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String text) {

        Timber.d("onQueryTextSubmit: %s", text);

        subject.onNext(text);
        return true;
      }

      @Override
      public boolean onQueryTextChange(String text) {

        Timber.d("onQueryTextChange: %s", text);

        subject.onNext(text);
        return true;
      }
    });

    return subject;
  }
}