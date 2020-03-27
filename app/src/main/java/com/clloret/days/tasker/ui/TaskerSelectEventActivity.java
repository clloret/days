package com.clloret.days.tasker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.R;
import com.clloret.days.domain.events.order.EventSortFactory.SortType;
import com.clloret.days.domain.events.order.EventSortable;
import com.clloret.days.domain.interactors.events.GetEventsUseCase;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import dagger.android.AndroidInjection;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import timber.log.Timber;

public class TaskerSelectEventActivity extends AppCompatActivity {

  public static final String EXTRA_EVENT = "com.clloret.days.extras.EXTRA_EVENT";

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();

  @Inject
  GetEventsUseCase getEventsUseCase;

  @Inject
  EventViewModelMapper eventViewModelMapper;

  @Inject
  Map<SortType, Comparator<EventSortable>> eventSortComparators;

  @BindView(R.id.listView)
  ListView listView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tasker_select_event);

    ButterKnife.bind(this);

    injectDependencies();

    getEvents();
  }

  @Override
  protected void onDestroy() {

    compositeDisposable.dispose();

    super.onDestroy();
  }

  private void getEvents() {

    Timber.d("Get events");

    Comparator<EventSortable> eventSortableComparator = eventSortComparators.get(SortType.NAME);

    if (eventSortableComparator == null) {
      return;
    }

    Disposable disposable = getEventsUseCase.execute(true)
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .toObservable()
        .concatMap(Observable::fromIterable)
        .sorted(eventSortableComparator)
        .toList()
        .map(eventViewModelMapper::fromEvent)
        .subscribe(this::showEvents, Timber::e);

    compositeDisposable.add(disposable);
  }

  private void showEvents(List<EventViewModel> list) {

    final ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
        list);
    listView.setAdapter(arrayAdapter);

    listView.setOnItemClickListener((adapterView, view, position, l) -> {
      final EventViewModel eventViewModel = list.get(position);

      final Intent intent = new Intent();
      intent.putExtra(EXTRA_EVENT, eventViewModel);
      setResult(Activity.RESULT_OK, intent);
      finish();
    });
  }

  private void injectDependencies() {

    AndroidInjection.inject(this);
  }
}
