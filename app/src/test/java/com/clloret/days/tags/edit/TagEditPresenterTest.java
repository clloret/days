package com.clloret.days.tags.edit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.RxImmediateSchedulerRule;
import com.clloret.days.model.AppDataStore;
import com.clloret.days.model.entities.Tag;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TagEditPresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private TagEditView tagEditView;

  private TagEditPresenter tagEditPresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    tagEditPresenter = new TagEditPresenter(appDataStore);
    tagEditPresenter.attachView(tagEditView);
  }

  @Test
  public void saveEvent_Always_CallApiAndNotifyView() {

    final Tag tag = new Tag("id", "Mock tag");

    when(appDataStore.editTag(any())).thenReturn(new Maybe<Tag>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Tag> observer) {

        observer.onSuccess(tag);
      }
    });

    tagEditPresenter.saveTag(tag);

    verify(appDataStore).editTag(any());
    verify(tagEditView).onSuccessfully(eq(tag));
  }

  @Test
  public void saveEvent_WhenEmptyEventName_NotifyViewError() {

    Tag event = new Tag("id", "");

    tagEditPresenter.saveTag(event);

    verify(tagEditView).onEmptyTagNameError();
  }
}