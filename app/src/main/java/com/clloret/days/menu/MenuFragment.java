package com.clloret.days.menu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import com.clloret.days.Navigator;
import com.clloret.days.R;
import com.clloret.days.base.BaseMvpFragment;
import com.clloret.days.menu.items.DrawerMenuItem;
import com.clloret.days.menu.items.DrawerTag;
import com.clloret.days.menu.items.DrawerTagSelectedMgr;
import com.clloret.days.model.entities.TagViewModel;
import com.clloret.days.utils.Optional;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;
import dagger.android.support.AndroidSupportInjection;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

public class MenuFragment extends BaseMvpFragment<MenuView, MenuPresenter>
    implements MenuView {

  @Inject
  Navigator navigator;

  @Inject
  SharedPreferences preferences;

  @Inject
  MenuPresenter injectPresenter;

  @BindView(R.id.listView)
  ListView listView;

  private DrawerTagSelectedMgr drawerTagSelectedMgr = new DrawerTagSelectedMgr();
  private MenuAdapter adapter;
  private DrawerLayout drawerLayout;

  private int previousCheckedPosition = -1;

  public MenuFragment() {

    super();

    // Required empty public constructor
  }

  @NonNull
  @Override
  public ViewState createViewState() {

    return new MenuViewState();
  }

  @Override
  public void onNewViewStateInstance() {

    loadData();
  }

  @NonNull
  @Override
  public MenuPresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setRetainInstance(true);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);

    Timber.d("onViewCreated");

    adapter = new MenuAdapter(getActivity(), drawerTagSelectedMgr);
    listView.setAdapter(adapter);

    listView.setOnItemLongClickListener((adapterView, v, i, l) -> {

      onLongClickMenuItem(i);

      return true;
    });

    listView.setOnItemClickListener((adapterView, v, i, l) -> onClickMenuItem(i));

    adapter.populateList();
  }

  @Override
  public void onDestroyView() {

    super.onDestroyView();
    adapter.dispose();
  }

  @Override
  protected void injectDependencies() {

    AndroidSupportInjection.inject(this);
  }

  private void onLongClickMenuItem(int position) {

    DrawerMenuItem drawerMenuItem = adapter.getItem(position);

    if (drawerMenuItem instanceof DrawerTag) {
      DrawerTag drawerTag = (DrawerTag) drawerMenuItem;
      presenter.editTag(drawerTag.getTag());
    }
  }

  private void onClickMenuItem(int position) {

    deselectPreviousMenuItem();
    selectMenuItem(position);

    drawerLayout.closeDrawers();
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
  }

  @Override
  public void setData(List<TagViewModel> data) {

    adapter.setData(data);
  }

  @Override
  public void showError(Throwable t) {

    showSnackbarMessage(t.getLocalizedMessage());
  }

  @Override
  public void showCreatedTag(TagViewModel tag) {

    adapter.addTag(tag);

    showSnackbarMessage(R.string.msg_tag_created);
  }

  @Override
  public void showEditTagUi(TagViewModel tag) {

    navigator.navigateToTagsEdit(getContext(), tag);
  }

  @Override
  public void updateSuccessfully(TagViewModel tag) {

    adapter.updateTag(tag);

    showSnackbarMessage(R.string.msg_tag_updated);
  }

  @Override
  public void deleteSuccessfully(TagViewModel tag) {

    adapter.deleteTag(tag);

    showSnackbarMessage(listView, R.string.msg_tag_removed);
  }

  @Override
  public void showTags(List<TagViewModel> data) {

    MenuViewState vs = ((MenuViewState) viewState);
    vs.setTags(data);

    setData(data);
  }

  public void loadData() {

    presenter.loadTags(false);
  }

  public void configure(DrawerLayout drawerLayout) {

    this.drawerLayout = drawerLayout;

    drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
  }

  public void showMainView() {

    String defaultList = preferences.getString(getString(R.string.pref_default_list), "0");
    int position = Integer.parseInt(defaultList);
    DrawerMenuItem drawerMenuItem = adapter.getItem(position);

    listView.setItemChecked(position, true);
    previousCheckedPosition = position;

    drawerMenuItem.select(getActivity());
  }

  private void selectMenuItem(int position) {

    DrawerMenuItem drawerMenuItem = adapter.getItem(position);

    listView.setItemChecked(position, drawerMenuItem.isSelectable());

    if (!drawerMenuItem.isSelectable()) {
      listView.setItemChecked(previousCheckedPosition, true);
    } else {
      previousCheckedPosition = position;
    }

    drawerMenuItem.select(getActivity());
  }

  private void deselectPreviousMenuItem() {

    if (previousCheckedPosition != -1) {
      DrawerMenuItem drawerMenuItemPrevious = adapter.getItem(previousCheckedPosition);
      drawerMenuItemPrevious.deselect();
    }
  }

  public Optional<TagViewModel> getSelectedTag() {

    Optional<DrawerTag> drawerTagOptional = drawerTagSelectedMgr.getSelected();

    if (drawerTagOptional.isPresent()) {
      return Optional.of(drawerTagOptional.get().getTag());
    } else {
      return Optional.empty();
    }
  }

}
