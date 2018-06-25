package com.clloret.days.events.edit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.RxImmediateSchedulerRule;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Event;
import com.clloret.days.model.entities.EventBuilder;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import java.util.Date;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventEditPresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private EventEditView eventEditView;

  private EventEditPresenter eventEditPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    // Get a reference to the class under test
    eventEditPresenter = new EventEditPresenter(appDataStore);
    eventEditPresenter.attachView(eventEditView);
  }

  @Test
  public void saveEvent_Always_CallApiAndNotifyView() {

    Event event = new EventBuilder()
        .setId("id")
        .setName("Mock Event")
        .setDate(new Date())
        .build();

    when(appDataStore.editEvent(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventEditPresenter.saveEvent(event);

    verify(appDataStore).editEvent(any());
    verify(eventEditView).onSuccessfully(eq(event));
  }

  @Test
  public void saveEvent_WhenEmptyEventName_NotifyViewError() {

    Event event = new EventBuilder()
        .setId("id")
        .setName("")
        .setDate(new Date())
        .build();

    eventEditPresenter.saveEvent(event);

    verify(eventEditView).onEmptyEventNameError();
  }
}