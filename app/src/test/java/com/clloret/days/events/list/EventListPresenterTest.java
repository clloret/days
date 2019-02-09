package com.clloret.days.events.list;

import static com.clloret.days.events.SampleBuilder.createEvent;
import static com.clloret.days.events.SampleBuilder.createEventList;
import static com.clloret.days.events.SampleBuilder.createEventViewModel;
import static io.reactivex.Single.just;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.device.TimeProviderImpl;
import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.events.filter.EventFilterByTag;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.utils.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EventListPresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private EventViewModelMapper eventViewModelMapper;

  @Mock
  private EventBus eventBus;

  @Mock
  private TimeProviderImpl timeProvider;

  @Mock
  private EventListView eventListView;

  @InjectMocks
  private EventListPresenter eventListPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

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

    eventListPresenter.setFilterStrategy(eventFilterByTag);
    eventListPresenter.loadEvents(false);

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

    addStubMethodsToMapper(event, eventViewModel);

    eventListPresenter.deleteEvent(eventViewModel);

    verify(appDataStore).deleteEvent(event);
    verify(eventListView).deleteSuccessfully(eventViewModel, true);
  }

  @Test
  public void makeEventFavorite_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();

    when(appDataStore.editEvent(event)).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    addStubMethodsToMapper(event, eventViewModel);

    eventListPresenter.makeEventFavorite(eventViewModel);

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

    when(timeProvider.getCurrentDate()).thenReturn(new LocalDate(2000, 1, 1));

    addStubMethodsToMapper(event, eventViewModel);

    eventListPresenter.resetDate(eventViewModel);

    verify(appDataStore).editEvent(any(Event.class));
    verify(eventListView).dateResetSuccessfully(any(EventViewModel.class));
  }

  private void addStubMethodsToMapper(Event event, EventViewModel eventViewModel) {

    when(eventViewModelMapper.fromEvent(Mockito.any(Event.class))).thenReturn(eventViewModel);
    when(eventViewModelMapper.toEvent(Mockito.any(EventViewModel.class))).thenReturn(event);
  }

}
