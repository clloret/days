package com.clloret.days.events.list;

import static io.reactivex.Single.just;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.support.annotation.NonNull;
import com.clloret.days.RxImmediateSchedulerRule;
import com.clloret.days.events.list.filter.EventFilterByTag;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.EventBuilder;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventListPresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private EventBus eventBus;

  @Mock
  private EventListView eventListView;

  private final List<Event> eventList = new ArrayList<>();
  private EventListPresenter eventListPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    eventListPresenter = new EventListPresenter(appDataStore, eventBus);
    eventListPresenter.attachView(eventListView);

    for (int i = 0; i < 5; i++) {
      eventList.add(new Event());
    }
  }

  @Test
  public void loadEvents_Always_CallApiAndNotifyView() {

    when(appDataStore.getEventsByTagId(any())).thenReturn(
        just(eventList)
    );

    String tagId = "recsOSmIyyMoUQiwn";
    EventFilterByTag eventFilterByTag = new EventFilterByTag(tagId);

    eventListPresenter.loadEvents(false, eventFilterByTag);

    verify(appDataStore).getEventsByTagId(tagId);
    verify(eventListView).setData(eventList);
    verify(eventListView).showContent();
  }

  @Test
  public void deleteEvent_Always_CallApiAndNotifyView() {

    final Event event = createEvent();

    when(appDataStore.deleteEvent(event)).thenReturn(new Maybe<Boolean>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Boolean> observer) {

        observer.onSuccess(true);
      }
    });

    eventListPresenter.deleteEvent(event);

    verify(appDataStore).deleteEvent(event);
    verify(eventListView).deleteSuccessfully(event, true);
  }

  @NonNull
  private Event createEvent() {

    return new EventBuilder()
        .setId("1")
        .setName("Mock Event")
        .setDate(new Date())
        .setFavorite(false)
        .build();
  }

  @Test
  public void makeEventFavorite_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final boolean favorite = event.isFavorite();

    when(appDataStore.editEvent(event)).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventListPresenter.makeEventFavorite(event);

    ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);

    verify(appDataStore).editEvent(argumentCaptor.capture());

    Event result = argumentCaptor.getValue();

    assertThat(result.isFavorite(), is(not(favorite)));

    //verify(appDataStore).editEvent(any(Event.class));
    verify(eventListView).favoriteSuccessfully(any(Event.class));
  }

  @Test
  public void resetDate_Always_CallApiAndNotifyView() {

    final Event event = createEvent();

    when(appDataStore.editEvent(event)).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventListPresenter.resetDate(event);

    ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);

    verify(appDataStore).editEvent(argumentCaptor.capture());

    Event result = argumentCaptor.getValue();
    Date date = LocalDate.now().toDate();
    assertThat(result.getDate(), equalTo(date));

    verify(eventListView).dateResetSuccessfully(any(Event.class));
  }

}
