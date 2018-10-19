package com.clloret.days.events.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.utils.SelectionMap;
import com.clloret.days.events.SampleBuilder;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.utils.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventCreatePresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private EventCreateView eventCreateView;

  private EventCreatePresenter eventCreatePresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    eventCreatePresenter = new EventCreatePresenter(appDataStore);
    eventCreatePresenter.attachView(eventCreateView);
  }

  @Test
  public void createEvent_Always_CallApiAndNotifyView() {

    final Event event = SampleBuilder.createEvent();
    final EventViewModel eventViewModel = SampleBuilder.createEventViewModel();

    when(appDataStore.createEvent(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventCreatePresenter
        .createEvent(SampleBuilder.name, SampleBuilder.description, SampleBuilder.date,
            new SelectionMap<>());

    verify(appDataStore).createEvent(any());
    verify(eventCreateView).onSuccessfully(eq(eventViewModel));
  }

  @Test
  public void createEvent_WhenEmptyName_NotifyViewError() {

    eventCreatePresenter
        .createEvent(SampleBuilder.emptyText, SampleBuilder.description, SampleBuilder.date,
            new SelectionMap<>());

    verify(eventCreateView).onEmptyEventNameError();
    verifyNoMoreInteractions(appDataStore);
  }
}