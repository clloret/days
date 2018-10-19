package com.clloret.days.tags.edit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.AppDataStore;
import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.tags.SampleBuilder;
import com.clloret.days.utils.RxImmediateSchedulerRule;
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

    Tag tag = SampleBuilder.createTag();
    TagViewModel tagViewModel = SampleBuilder.createTagViewModel();

    when(appDataStore.editTag(any())).thenReturn(new Maybe<Tag>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Tag> observer) {

        observer.onSuccess(tag);
      }
    });

    tagEditPresenter.saveTag(tagViewModel);

    verify(appDataStore).editTag(any());
    verify(tagEditView).onSuccessfully(eq(tagViewModel));
  }

  @Test
  public void saveEvent_WhenEmptyEventName_NotifyViewError() {

    TagViewModel tagViewModel = SampleBuilder.createTagViewModel();
    tagViewModel.setName("");

    tagEditPresenter.saveTag(tagViewModel);

    verify(tagEditView).onEmptyTagNameError();
  }
}