package com.clloret.days.events.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.interactors.events.CreateEventUseCase;
import com.clloret.days.domain.interactors.tags.GetTagsUseCase;
import com.clloret.days.events.SampleBuilder;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.entities.mapper.TagViewModelMapper;
import com.clloret.test_android_common.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

@SuppressWarnings({"PMD.UnusedPrivateField", "unused"})
public class EventCreatePresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule SCHEDULERS = new RxImmediateSchedulerRule();

  @Mock
  private GetTagsUseCase getTagsUseCase;

  @Mock
  private CreateEventUseCase createEventUseCase;

  @Mock
  private TagViewModelMapper tagViewModelMapper;

  @Mock
  private EventCreateView eventCreateView;

  @Spy
  private Scheduler uiThread = Schedulers.trampoline();

  @InjectMocks
  private EventCreatePresenter eventCreatePresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    eventCreatePresenter.attachView(eventCreateView);
  }

  @Test
  public void createEvent_Always_CallApiAndNotifyView() {

    final Event event = SampleBuilder.createEvent();
    final EventViewModel eventViewModel = SampleBuilder.createEventViewModel();

    when(createEventUseCase.execute(any())).thenReturn(new Maybe<Event>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Event> observer) {

        observer.onSuccess(event);
      }
    });

    eventCreatePresenter.createEvent(eventViewModel);

    verify(createEventUseCase).execute(any());
    verify(eventCreateView).onSuccessfully(eq(eventViewModel));
  }

  @Test
  public void createEvent_WhenEmptyName_NotifyViewError() {

    final EventViewModel eventViewModel = SampleBuilder.createEventViewModel();
    eventViewModel.setName(SampleBuilder.EMPTY_TEXT);

    eventCreatePresenter.createEvent(eventViewModel);

    verify(eventCreateView).onEmptyEventNameError();
    verifyNoMoreInteractions(createEventUseCase);
  }
}