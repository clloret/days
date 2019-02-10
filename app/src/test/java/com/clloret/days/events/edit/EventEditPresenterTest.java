package com.clloret.days.events.edit;

import static com.clloret.days.events.SampleBuilder.createEvent;
import static com.clloret.days.events.SampleBuilder.createEventViewModel;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.DeleteEventUseCase;
import com.clloret.days.domain.interactors.events.EditEventUseCase;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.events.SampleBuilder;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.EventViewModelMapper;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.clloret.days.utils.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class EventEditPresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private GetTagsUseCase getTagsUseCase;

  @Mock
  private EditEventUseCase editEventUseCase;

  @Mock
  private DeleteEventUseCase deleteEventUseCase;

  @Mock
  private EventViewModelMapper eventViewModelMapper;

  @Mock
  private TagViewModelMapper tagViewModelMapper;

  @Mock
  private EventEditView eventEditView;

  @InjectMocks
  private EventEditPresenter eventEditPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    eventEditPresenter.attachView(eventEditView);
  }

  @Test
  public void saveEvent_Always_CallApiAndNotifyView() {

    final Event event = createEvent();
    final EventViewModel eventViewModel = createEventViewModel();

    when(editEventUseCase.execute(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    addStubMethodsToMapper(event, eventViewModel);

    eventEditPresenter.saveEvent(eventViewModel, eventViewModel);

    verify(editEventUseCase).execute(any());
    verify(eventEditView).onSuccessfully(eq(eventViewModel));
  }

  private void addStubMethodsToMapper(Event event, EventViewModel eventViewModel) {

    when(eventViewModelMapper.fromEvent(Mockito.any(Event.class))).thenReturn(eventViewModel);
    when(eventViewModelMapper.toEvent(Mockito.any(EventViewModel.class))).thenReturn(event);
  }

  @Test
  public void saveEvent_WhenEmptyEventName_NotifyViewError() {

    final EventViewModel eventViewModel = createEventViewModel();
    eventViewModel.setName(SampleBuilder.emptyText);

    eventEditPresenter.saveEvent(eventViewModel, eventViewModel);

    verify(eventEditView).onEmptyEventNameError();
  }
}