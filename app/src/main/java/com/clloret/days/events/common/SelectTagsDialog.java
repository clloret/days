package com.clloret.days.events.common;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.clloret.days.R;
import com.clloret.days.model.entities.TagViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SelectTagsDialog extends DialogFragment {

  private static final String BUNDLE_TITLE = "title";
  private static final String BUNDLE_NAME_TAGS = "nameTags";
  private static final String BUNDLE_CHECKED_TAGS = "checkedTags";
  private static final String BUNDLE_TAGS = "tags";

  public interface SelectTagsDialogListener {

    void onFinishTagsDialog(Collection<TagViewModel> selectedItems);
  }

  public SelectTagsDialog() {

    super();
    // Empty constructor required for DialogFragment
  }

  public static SelectTagsDialog newInstance(String title,
      String[] nameTags, boolean[] checkedTags, List<TagViewModel> tags) {

    Bundle args = new Bundle();

    args.putString(BUNDLE_TITLE, title);
    args.putStringArray(BUNDLE_NAME_TAGS, nameTags);
    args.putBooleanArray(BUNDLE_CHECKED_TAGS, checkedTags);
    args.putParcelableArrayList(BUNDLE_TAGS, new ArrayList<>(tags));

    SelectTagsDialog dialog = new SelectTagsDialog();
    dialog.setArguments(args);

    return dialog;
  }


  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    final Bundle args = Objects.requireNonNull(getArguments());
    final String title = args.getString(BUNDLE_TITLE);
    final String[] nameTags = args.getStringArray(BUNDLE_NAME_TAGS);
    final boolean[] checkedTags = args.getBooleanArray(BUNDLE_CHECKED_TAGS);
    final List<TagViewModel> tags = args.getParcelableArrayList(BUNDLE_TAGS);
    final Collection<TagViewModel> selectedItems = getSelectedTags(checkedTags, tags);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(title)
        .setMultiChoiceItems(nameTags, checkedTags,
            (dialog, which, isChecked) -> {

              TagViewModel tag = tags.get(which);
              if (isChecked) {
                selectedItems.add(tag);
              } else {
                selectedItems.remove(tag);
              }
            })
        .setPositiveButton(getString(R.string.action_ok), (dialog, id) -> {

          SelectTagsDialogListener listener = (SelectTagsDialogListener) getActivity();
          listener.onFinishTagsDialog(selectedItems);

        })
        .setNegativeButton(getString(R.string.action_cancel), (dialog, id) -> {

        });

    return builder.create();
  }

  private List<TagViewModel> getSelectedTags(boolean[] checkedTags, List<TagViewModel> tags) {

    final List<TagViewModel> selectedItems = new ArrayList<>();

    for (int i = 0; i < checkedTags.length; i++) {
      if (checkedTags[i]) {
        selectedItems.add(tags.get(i));
      }
    }

    return selectedItems;
  }
}
