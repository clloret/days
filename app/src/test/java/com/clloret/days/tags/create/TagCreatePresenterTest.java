package com.clloret.days.tags.create;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

public class TagCreatePresenterTest {

  @ClassRule
  public static final RxImmediateSchedulerRule schedulers = new RxImmediateSchedulerRule();

  @Mock
  private AppDataStore appDataStore;

  @Mock
  private TagCreateView tagCreateView;

  private TagCreatePresenter tagCreatePresenter;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    tagCreatePresenter = new TagCreatePresenter(appDataStore);
    tagCreatePresenter.attachView(tagCreateView);
  }

  @Test
  public void createTag_Always_CallApiAndNotifyView() {

    String name = "Mock tag";

    final Tag tag = new Tag("id", name);

    when(appDataStore.createTag(any())).thenReturn(new Maybe<Tag>() {
      @Override
      protected void subscribeActual(MaybeObserver<? super Tag> observer) {

        observer.onSuccess(tag);
      }
    });

    tagCreatePresenter.createTag(name);

    verify(appDataStore).createTag(any());
    verify(tagCreateView).onSuccessfully(eq(tag));
  }

  @Test
  public void createTag_WhenEmptyName_NotifyViewError() {

    String name = "";
    tagCreatePresenter.createTag(name);

    verify(tagCreateView).onEmptyTagNameError();
    verifyNoMoreInteractions(appDataStore);
  }

}