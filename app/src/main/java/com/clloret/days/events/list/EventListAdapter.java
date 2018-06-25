package com.clloret.days.events.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.clloret.days.R;
import com.clloret.days.events.list.EventListAdapter.EventViewHolder;
import com.clloret.days.model.entities.Event;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {

  private final OnListAdapterListener listener;
  private List<Event> events;
  private Comparator<Event> currentComparator;

  public EventListAdapter(Comparator<Event> comparator, OnListAdapterListener listener) {

    this.currentComparator = comparator;
    this.listener = listener;
    this.setHasStableIds(true);
  }

  public void onItemRemove(ViewHolder viewHolder) {

    final int position = viewHolder.getAdapterPosition();

    final Event deletedItem = events.get(position);

    listener.onDeleteItem(deletedItem);
  }

  public int addItem(Event item) {

    if (events.add(item)) {

      Collections.sort(events, currentComparator);

      int index = events.indexOf(item);
      notifyItemInserted(index);

      return index;
    }

    return -1;
  }

  public void removeItem(Event item) {

    int index = events.indexOf(item);
    if (index != -1) {
      events.remove(index);
      notifyItemRemoved(index);
    }
  }

  public void restoreItem(Event item) {

    if (events.add(item)) {
      Collections.sort(events, currentComparator);

      int position = events.indexOf(item);

      notifyItemInserted(position);
    }
  }

  public void updateItem(Event item) {

    int index = events.indexOf(item);
    if (index != -1) {
      events.set(index, item);

      Collections.sort(events, currentComparator);

      int position = events.indexOf(item);

      notifyItemChanged(position);
    }
  }

  public List<Event> getEvents() {

    return events;
  }

  public void setEvents(List<Event> events) {

    Collections.sort(events, currentComparator);

    this.events = events;
  }

  @NonNull
  @Override
  public EventViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

    final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

    return new EventViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

    holder.bindData(events.get(position));
  }

  @Override
  public int getItemViewType(int position) {

    return R.layout.fragment_event;
  }

  @Override
  public long getItemId(int position) {

    return events.get(position)
        .hashCode(); // need to return stable (= not change even after position changed) value
  }

  @Override
  public int getItemCount() {

    return events == null ? 0 : events.size();
  }

  public void sortByComparator(Comparator<Event> comparator) {

    currentComparator = comparator;

    Collections.sort(events, comparator);

    notifyDataSetChanged();
  }

  public interface OnListAdapterListener {

    void onSelectItem(Event item);

    void onDeleteItem(Event item);

    void onResetItem(Event item);

    void onFavoriteItem(Event item);
  }

  class EventViewHolder extends RecyclerView.ViewHolder {

    public View viewBackground;
    public View viewForeground;
    public TextView name;
    private View itemView;
    private TextView days;
    private ImageView favoriteButton;
    private ImageView resetButton;

    public EventViewHolder(final View view) {

      super(view);
      itemView = view;
      name = view.findViewById(R.id.textview_event_name);
      days = view.findViewById(R.id.textview_event_days);
      favoriteButton = view.findViewById(R.id.favorite_button);
      resetButton = view.findViewById(R.id.reset_button);
      viewBackground = view.findViewById(R.id.view_background);
      viewForeground = view.findViewById(R.id.view_foreground);
    }

    public void bindData(final Event viewModel) {

      int days = viewModel.getDaysSince();

      name.setText(viewModel.getName());
      this.days.setText(String.valueOf(days));

      favoriteButton.setImageResource(viewModel.isFavorite() ? R.drawable.ic_favorite_24dp
          : R.drawable.ic_favorite_border_24dp);

      itemView.setOnClickListener(v -> {

        if (null != listener) {
          listener.onSelectItem(viewModel);
        }
      });

      resetButton.setOnClickListener(view -> listener.onResetItem(viewModel));
      favoriteButton.setOnClickListener(view -> listener.onFavoriteItem(viewModel));
    }
  }
}