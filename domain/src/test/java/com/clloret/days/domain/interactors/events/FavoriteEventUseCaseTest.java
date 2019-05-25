package com.clloret.days.domain.interactors.events;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.domain.repository.EventRepository;
import com.clloret.days.domain.utils.RxImmediateThreadingSchedulers;
import com.clloret.days.domain.utils.ThreadSchedulers;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class FavoriteEventUseCaseTest {

  @Mock
  private EventRepository dataStore;

  @Spy
  private ThreadSchedulers threadSchedulers = new RxImmediateThreadingSchedulers();

  @InjectMocks
  private FavoriteEventUseCase sut;

  private void addMethodStubs(Event event) {

    CommonMocksInteractions.addDataStoreStubs(dataStore, event);
  }

  private void verifyDataStoreMockInteractions(Event event) {

    Mockito.verify(dataStore, Mockito.times(1))
        .edit(event);
    Mockito.verifyNoMoreInteractions(dataStore);
  }

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void execute_WhenEventIsNotFavorite_MakeEventFavorite() {

    Event event = new EventBuilder()
        .setFavorite(false)
        .build();

    addMethodStubs(event);

    TestObserver<Event> testObserver = sut.execute(event).test();

    verifyDataStoreMockInteractions(event);

    testObserver
        .assertComplete()
        .assertNoErrors()
        .assertValue(Event::isFavorite);
  }
}