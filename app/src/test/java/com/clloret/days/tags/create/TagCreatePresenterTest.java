package com.clloret.days.tags.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.domain.interactors.tags.CreateTagUseCase;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.tags.SampleBuilder;
import com.clloret.test_android_common.RxImmediateSchedulerRule;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TagCreatePresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule SCHEDULERS = new RxImmediateSchedulerRule();

  @Mock
  private CreateTagUseCase createTagUseCase;

  @Mock
  private TagCreateView tagCreateView;

  @InjectMocks
  private TagCreatePresenter tagCreatePresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.openMocks(this);

    tagCreatePresenter.attachView(tagCreateView);
  }

  @Test
  public void createTag_Always_CallApiAndNotifyView() {

    Tag tag = SampleBuilder.createTag();
    TagViewModel tagViewModel = SampleBuilder.createTagViewModel();

    when(createTagUseCase.execute(any()))
        .thenReturn(new Maybe<Tag>() {
          @Override
          protected void subscribeActual(MaybeObserver<? super Tag> observer) {

            observer.onSuccess(tag);
          }
        });

    tagCreatePresenter.createTag(SampleBuilder.NAME);

    verify(createTagUseCase).execute(any());
    verify(tagCreateView).onSuccessfully(eq(tagViewModel));
  }

  @Test
  public void createTag_WhenEmptyName_NotifyViewError() {

    tagCreatePresenter.createTag(SampleBuilder.EMPTY_TEXT);

    verify(tagCreateView).onEmptyTagNameError();
    verifyNoMoreInteractions(createTagUseCase);
  }

}