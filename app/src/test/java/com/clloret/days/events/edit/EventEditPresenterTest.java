package com.clloret.days.events.edit;

import static com.clloret.days.events.SampleBuilder.createEvent;
import static com.clloret.days.events.SampleBuilder.createEventViewModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Event;
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

    eventEditPresenter = new EventEditPresenter(appDataStore);
    eventEditPresenter.attachView(eventEditView);
  }

  @Test
  public void saveEvent_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();

    when(appDataStore.editEvent(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventEditPresenter.saveEvent(event);

    verify(appDataStore).editEvent(any());
    verify(eventEditView).onSuccessfully(eq(eventViewModel));
  }

  @Test
  public void saveEvent_WhenEmptyEventName_NotifyViewError() {

    final Event event = createEvent();
    event.setName(SampleBuilder.emptyText);

    eventEditPresenter.saveEvent(event);

    verify(eventEditView).onEmptyEventNameError();
  }
}