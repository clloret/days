package com.clloret.days.tags.create;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.events.ShowMessageEvent;
import com.clloret.days.model.events.TagCreatedEvent;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

public class TagCreateActivity extends
    BaseMvpActivity<TagCreateView, TagCreatePresenter> implements TagCreateView {

  @Inject
  TagCreatePresenter injectPresenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @BindView(R.id.layout_tagdetail_name)
  TextInputLayout nameLayout;

  @BindView(R.id.edittext_tagdetail_name)
  EditText nameEdit;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_detail);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    showSoftKeyboard();
  }

  @Override
  protected void injectDependencies() {

    AndroidInjection.inject(this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_tag_details, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();

    switch (id) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;

      case R.id.menu_save:
        saveTag();
        return true;

      case R.id.menu_delete:
        discardTag();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @NonNull
  @Override
  public TagCreatePresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onSuccessfully(TagViewModel tag) {

    EventBus.getDefault().post(new TagCreatedEvent(tag));
    finish();
  }

  @Override
  public void onError(String message) {

    showToastMessage(message);
  }

  @Override
  public void onEmptyTagNameError() {

    nameLayout.setError(getString(R.string.msg_error_tag_name_required));
    nameLayout.setErrorEnabled(true);
    nameLayout.getEditText().requestFocus();
  }

  @Override
  public void showIndeterminateProgress() {

    progressBar.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideIndeterminateProgress() {

    progressBar.setVisibility(View.INVISIBLE);
  }

  private void discardTag() {

    EventBus.getDefault().post(new ShowMessageEvent(getString(R.string.msg_tag_discarded)));

    NavUtils.navigateUpFromSameTask(this);
  }

  private void saveTag() {

    String name = nameEdit.getText().toString();

    presenter.createTag(name);
  }

}
