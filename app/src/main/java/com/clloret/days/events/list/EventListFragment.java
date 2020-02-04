package com.clloret.days.events.list;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import com.clloret.days.Navigator;
import com.clloret.days.R;
import com.clloret.days.base.BaseLceFragment;
import com.clloret.days.domain.events.EventPeriodFormat;
import com.clloret.days.domain.events.filter.EventFilterStrategy;
import com.clloret.days.domain.events.order.EventSortFactory.SortType;
import com.clloret.days.domain.events.order.EventSortable;
import com.clloret.days.domain.utils.PreferenceUtils;
import com.clloret.days.model.entities.EventViewModel;
import com.clloret.days.model.events.RefreshRequestEvent;
import com.google.android.material.snackbar.Snackbar;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;
import dagger.android.support.AndroidSupportInjection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import timber.log.Timber;

@SuppressWarnings("PMD.TooManyMethods")
public class EventListFragment
    extends
    BaseLceFragment<SwipeRefreshLayout, List<EventViewModel>, EventListView, EventListPresenter>
    implements EventListView, SwipeRefreshLayout.OnRefreshListener,
    EventListAdapter.OnListAdapterListener {

  private static final String BUNDLE_FILTER_STRATEGY = "FILTER_STRATEGY";

  private static final Map<Integer, SortType> MAP_MENU_ID_SORT_TYPE = Collections.unmodifiableMap(
      new HashMap<Integer, SortType>() {
        {
          put(R.id.menu_sort_name, SortType.NAME);
          put(R.id.menu_sort_favorite, SortType.FAVORITE);
          put(R.id.menu_sort_latest_date, SortType.LATEST_DATE);
          put(R.id.menu_sort_oldest_date, SortType.OLDEST_DATE);
        }
      });

  private static final Map<SortType, Integer> MAP_SORT_TYPE_MENU_ID = Collections.unmodifiableMap(
      new HashMap<SortType, Integer>() {
        {
          put(SortType.NAME, R.id.menu_sort_name);
          put(SortType.FAVORITE, R.id.menu_sort_favorite);
          put(SortType.LATEST_DATE, R.id.menu_sort_latest_date);
          put(SortType.OLDEST_DATE, R.id.menu_sort_oldest_date);
        }
      });

  @Inject
  Navigator navigator;

  @Inject
  PreferenceUtils preferenceUtils;

  @Inject
  EventListPresenter injectPresenter;

  @Inject
  Map<SortType, Comparator<EventSortable>> eventSortComparators;

  @Inject
  EventPeriodFormat eventPeriodFormat;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.emptyView)
  View emptyView;

  private SearchView searchView;
  private EventListAdapter adapter;
  private SortType savedSortType;
  private OnProgressListener progressListener;
  private OnFragmentLifecycleListener tagSelectedListener;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public EventListFragment() {

    super();

    // Mandatory empty constructor for the fragment manager
  }

  @NonNull
  @Override
  public LceViewState<List<EventViewModel>, EventListView> createViewState() {

    return new RetainingLceViewState<>();
  }

  @Override
  public void showContent() {

    super.showContent();

    contentView.setRefreshing(false);

    checkIfEmptyViewToBeDisplayed();
  }

  @Override
  public void showError(Throwable e, boolean pullToRefresh) {

    super.showError(e, pullToRefresh);

    emptyView.setVisibility(View.GONE);
  }

  @Override
  public void showLoading(boolean pullToRefresh) {

    super.showLoading(pullToRefresh);

    emptyView.setVisibility(View.GONE);
  }

  @Override
  public void onNewViewStateInstance() {

    showLoading(false);
    loadData(false);
  }

  @Override
  public List<EventViewModel> getData() {

    Timber.d("getData");

    return adapter == null ? null : adapter.getEvents();
  }

  @Override
  public void setData(List<EventViewModel> data) {

    adapter.setEvents(data);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void loadData(boolean pullToRefresh) {

    presenter.loadEvents(pullToRefresh);
  }

  @NonNull
  @Override
  public EventListPresenter createPresenter() {

    return injectPresenter;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);
    setRetainInstance(true);
  }

  @Override
  public void onStart() {

    super.onStart();

    Timber.d("onStart");

    if (tagSelectedListener != null) {
      tagSelectedListener.onStartFragment();
    }
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    super.onViewCreated(view, savedInstanceState);

    Timber.d("onViewCreated");

    readBundle(getArguments());

    contentView.setOnRefreshListener(this);

    createAdapter();

    configureRecyclerView();
  }

  @Override
  protected void injectDependencies() {

    AndroidSupportInjection.inject(this);
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    return inflater.inflate(R.layout.fragment_event_list, container, false);
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.menu_event_list, menu);

    configureSearchView(menu);
  }

  @Override
  public void onPrepareOptionsMenu(@NonNull Menu menu) {

    super.onPrepareOptionsMenu(menu);

    selectMenuSortMode(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int itemId = item.getItemId();

    if (sortByComparator(itemId)) {

      item.setChecked(true);
      return true;
    } else {

      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onRefresh() {

    Timber.d("onRefresh");

    EventBus.getDefault().post(new RefreshRequestEvent());

    loadData(true);
  }

  @Override
  protected String getErrorMessage(Throwable e, boolean pullToRefresh) {

    return e.getLocalizedMessage();
  }

  @Override
  public void onSelectItem(EventViewModel item) {

    presenter.editEvent(item);
  }

  @Override
  public void onDeleteItem(EventViewModel item) {

    presenter.deleteEvent(item);
  }

  @Override
  public void onResetItem(EventViewModel item) {

    presenter.resetDate(item);
  }

  @Override
  public void onFavoriteItem(EventViewModel item) {

    presenter.makeEventFavorite(item);
  }

  @Override
  public void onToggleNotificationsItem(EventViewModel item) {

    presenter.toggleEventReminder(item);
  }

  @Override
  public void onError(String message) {

    Timber.e(message);

    contentView.setRefreshing(false);

    showSnackbarMessage(message);
  }

  @Override
  public void showMessage(String message) {

    showSnackbarMessage(message);
  }

  @Override
  public void showIndeterminateProgress() {

    if (progressListener != null) {
      progressListener.showIndeterminateProgress();
    }
  }

  @Override
  public void hideIndeterminateProgress() {

    if (progressListener != null) {
      progressListener.hideIndeterminateProgress();
    }
  }

  @Override
  public void showEditEventUi(EventViewModel event) {

    navigator.navigateToEventEdit(getContext(), event);
  }

  @Override
  public void showCreatedEvent(EventViewModel eventViewModel) {

    addCreatedEventToAdapter(eventViewModel);
    checkIfEmptyViewToBeDisplayed();
  }

  @Override
  public void createSuccessfully(EventViewModel event) {

    showSnackbarMessage(recyclerView, R.string.msg_event_created);
  }

  @Override
  public void deleteSuccessfully(EventViewModel event, boolean deleted) {

    if (deleted) {

      adapter.removeItem(event);

      Context context = getContext();
      String text = context.getString(R.string.msg_event_deleted);
      Snackbar snackbar = Snackbar.make(recyclerView, text, Snackbar.LENGTH_LONG);
      snackbar
          .setAction(R.string.action_undo, view -> presenter.undoDelete(event));
      snackbar.setActionTextColor(Color.RED);
      snackbar.show();

      checkIfEmptyViewToBeDisplayed();

    } else {
      showSnackbarMessage(R.string.msg_error_event_could_not_be_deleted);
    }
  }

  @Override
  public void undoDeleteSuccessfully(EventViewModel event) {

    adapter.restoreItem(event);

    checkIfEmptyViewToBeDisplayed();
  }

  @Override
  public void updateSuccessfully(EventViewModel event) {

    adapter.updateItem(event);

    showSnackbarMessage(recyclerView, R.string.msg_event_updated);
  }

  @Override
  public void favoriteSuccessfully(EventViewModel event) {

    adapter.updateItem(event);
  }

  @Override
  public void dateResetSuccessfully(EventViewModel event) {

    adapter.updateItem(event);
  }

  @Override
  public void reminderSuccessfully(EventViewModel event) {

    adapter.updateItem(event);
  }

  public static EventListFragment newInstance(@NonNull EventFilterStrategy eventFilterStrategy) {

    Bundle bundle = new Bundle();
    bundle.putSerializable(BUNDLE_FILTER_STRATEGY, eventFilterStrategy);

    EventListFragment fragment = new EventListFragment();
    fragment.setArguments(bundle);

    return fragment;
  }

  private void createAdapter() {

    Comparator<EventSortable> comparator = getEventSortableComparator();

    adapter = new EventListAdapter(comparator, eventPeriodFormat, this);
  }

  private Comparator<EventSortable> getEventSortableComparator() {

    final int sortMode = preferenceUtils.getSortMode();
    savedSortType = SortType.fromValue(sortMode);
    return eventSortComparators.get(savedSortType);
  }

  private void configureRecyclerView() {

    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);

    ItemTouchHelper.Callback callback =
        new EventListTouchHelperCallback(adapter);
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(recyclerView);
  }

  private void selectMenuSortMode(Menu menu) {

    int menuId = MAP_SORT_TYPE_MENU_ID.get(savedSortType);
    menu.findItem(menuId).setChecked(true);
  }

  private boolean sortByComparator(int itemId) {

    switch (itemId) {
      case R.id.menu_sort_name:
      case R.id.menu_sort_favorite:
      case R.id.menu_sort_latest_date:
      case R.id.menu_sort_oldest_date:
        final SortType sortType = MAP_MENU_ID_SORT_TYPE.get(itemId);
        final Comparator<EventSortable> comparator = eventSortComparators.get(sortType);
        adapter.sortByComparator(comparator);
        preferenceUtils.setSortMode(sortType);
        return true;

      default:
        return false;
    }
  }

  private void checkIfEmptyViewToBeDisplayed() {

    if (adapter.getItemCount() == 0) {
      emptyView.setVisibility(View.VISIBLE);
    } else {
      emptyView.setVisibility(View.GONE);
    }
  }

  private void addCreatedEventToAdapter(EventViewModel event) {

    int index = adapter.addItem(event);
    recyclerView.scrollToPosition(index);
  }

  private void readBundle(Bundle bundle) {

    if (bundle != null) {
      EventFilterStrategy filterStrategy = (EventFilterStrategy) bundle
          .getSerializable(BUNDLE_FILTER_STRATEGY);
      presenter.setFilterStrategy(filterStrategy);
    }
  }

  private void configureSearchView(@NonNull Menu menu) {

    final MenuItem searchItem = menu.findItem(R.id.menu_search_events);

    if (searchItem != null) {
      searchView = (SearchView) searchItem.getActionView();
    }

    final FragmentActivity activity = Objects.requireNonNull(getActivity());
    final SearchManager searchManager = (SearchManager)
        activity.getSystemService(Context.SEARCH_SERVICE);

    if (searchView != null) {
      searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.getComponentName()));

      presenter.observeSearchQuery(RxSearchObservable.fromView(searchView));
    }
  }

  public void setOnProgressListener(OnProgressListener listener) {

    progressListener = listener;
  }

  public void setOnFragmentLifecycleListener(OnFragmentLifecycleListener listener) {

    tagSelectedListener = listener;
  }

  public interface OnProgressListener {

    void showIndeterminateProgress();

    void hideIndeterminateProgress();
  }

  public interface OnFragmentLifecycleListener {

    void onStartFragment();
  }

}
