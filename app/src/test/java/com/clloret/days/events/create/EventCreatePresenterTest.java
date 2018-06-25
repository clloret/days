package com.clloret.days.events.create;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.clloret.days.RxImmediateSchedulerRule;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.EventBuilder;
import com.clloret.days.utils.SelectionMap;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import java.util.Date;
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

    String name = "Mock event";
    String description = "Description";
    Date date = new Date();

    Event event = new EventBuilder()
        .setName(name)
        .setDescription(description)
        .setDate(date)
        .build();

    when(appDataStore.createEvent(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventCreatePresenter.createEvent(name, description, date, new SelectionMap<>());

    verify(appDataStore).createEvent(any());
    verify(eventCreateView).onSuccessfully(eq(event));
  }

  @Test
  public void createEvent_WhenEmptyName_NotifyViewError() {

    String name = "";
    String description = "Description";
    Date date = new Date();
    eventCreatePresenter.createEvent(name, description, date, new SelectionMap<>());

    verify(eventCreateView).onEmptyEventNameError();
    verifyNoMoreInteractions(appDataStore);
  }
}