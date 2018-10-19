package com.clloret.days.events.list;

import static com.clloret.days.events.SampleBuilder.createEvent;
import static com.clloret.days.events.SampleBuilder.createEventList;
import static com.clloret.days.events.SampleBuilder.createEventViewModel;
import static io.reactivex.Single.just;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.events.list.filter.EventFilterByTag;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.utils.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import java.util.Date;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
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

  private EventListPresenter eventListPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    eventListPresenter = new EventListPresenter(appDataStore, eventBus);
    eventListPresenter.attachView(eventListView);
  }

  @Test
  public void loadEvents_Always_CallApiAndNotifyView() {

    List<Event> eventList = createEventList();

    when(appDataStore.getEventsByTagId(any())).thenReturn(
        just(eventList)
    );

    String tagId = "recsOSmIyyMoUQiwn";
    EventFilterByTag eventFilterByTag = new EventFilterByTag(tagId);

    eventListPresenter.loadEvents(false, eventFilterByTag);

    verify(appDataStore).getEventsByTagId(tagId);
    verify(eventListView).setData(ArgumentMatchers.anyList());
    verify(eventListView).showContent();
  }

  @Test
  public void deleteEvent_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();

    when(appDataStore.deleteEvent(event)).thenReturn(new Maybe<Boolean>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Boolean> observer) {

        observer.onSuccess(true);
      }
    });

    eventListPresenter.deleteEvent(eventViewModel);

    verify(appDataStore).deleteEvent(event);
    verify(eventListView).deleteSuccessfully(eventViewModel, true);
  }

  @Test
  public void makeEventFavorite_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();
    final boolean favorite = event.isFavorite();

    when(appDataStore.editEvent(event)).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventListPresenter.makeEventFavorite(eventViewModel);

    ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);

    verify(appDataStore).editEvent(argumentCaptor.capture());

    Event result = argumentCaptor.getValue();

    assertThat(result.isFavorite(), is(not(favorite)));

    verify(appDataStore).editEvent(any(Event.class));
    verify(eventListView).favoriteSuccessfully(any(EventViewModel.class));
  }

  @Test
  public void resetDate_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();

    when(appDataStore.editEvent(event)).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventListPresenter.resetDate(eventViewModel);

    ArgumentCaptor<Event> argumentCaptor = ArgumentCaptor.forClass(Event.class);

    verify(appDataStore).editEvent(argumentCaptor.capture());

    Event result = argumentCaptor.getValue();
    Date date = LocalDate.now().toDate();
    assertThat(result.getDate(), equalTo(date));

    verify(eventListView).dateResetSuccessfully(any(EventViewModel.class));
  }

}
