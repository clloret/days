package com.clloret.days.tags.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpActivity;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.model.events.TagDeletedEvent;
import com.clloret.days.model.events.TagModifiedEvent;
import com.google.android.material.textfield.TextInputLayout;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;

public class TagEditActivity extends
    BaseMvpActivity<TagEditView, TagEditPresenter> implements TagEditView {

  private static final String EXTRA_TAG = "com.clloret.days.extras.EXTRA_TAG";

  @Inject
  TagEditPresenter injectPresenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.progressBar)
  ProgressBar progressBar;

  @BindView(R.id.layout_tagdetail_name)
  TextInputLayout nameLayout;

  @BindView(R.id.edittext_tagdetail_name)
  EditText editName;

  private TagViewModel tag;

  public static Intent getCallingIntent(Context context, TagViewModel tag) {

    Intent intent = new Intent(context, TagEditActivity.class);
    intent.putExtra(EXTRA_TAG, tag);

    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_detail);

    ButterKnife.bind(this);

    configureActionBar(toolbar);

    tag = getIntent().getParcelableExtra(EXTRA_TAG);

    editName.setText(tag.getName());
    editName.setSelection(editName.getText().length());
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
        deleteTag();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @NonNull
  @Override
  public TagEditPresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onSuccessfully(TagViewModel tag) {

    EventBus.getDefault().post(new TagModifiedEvent(tag));
    finish();
  }

  @Override
  public void deleteSuccessfully(TagViewModel tag, boolean deleted) {

    if (deleted) {
      EventBus.getDefault().post(new TagDeletedEvent(tag));
      finish();
    } else {
      showSnackbarMessage(R.string.msg_error_tag_could_not_be_deleted);
    }
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

  private void deleteTag() {

    presenter.deleteTag(tag);
  }

  private void saveTag() {

    String name = editName.getText().toString();

    tag.setName(name);

    presenter.saveTag(tag);
  }
}
