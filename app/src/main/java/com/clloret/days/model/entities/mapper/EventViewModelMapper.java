package com.clloret.days.model.entities.mapper;

import com.clloret.days.domain.entities.Event;
import com.clloret.days.domain.entities.EventBuilder;
import com.clloret.days.model.entities.EventViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventViewModelMapper {

  public Event toEvent(EventViewModel viewModel) {

    Event event = null;
    if (viewModel != null) {
      event = new EventBuilder()
          .setId(viewModel.getId())
          .setName(viewModel.getName())
          .setDescription(viewModel.getDescription())
          .setDate(viewModel.getDate())
          .setFavorite(viewModel.isFavorite())
          .setTags(viewModel.getTags())
          .build();
    }
    return event;
  }

  public EventViewModel fromEvent(Event event) {

    EventViewModel viewModel = null;
    if (event != null) {
      viewModel = new EventViewModel();
      viewModel.setId(event.getId());
      viewModel.setName(event.getName());
      viewModel.setDescription(event.getDescription());
      viewModel.setDate(event.getDate());
      viewModel.setFavorite(event.isFavorite());
      viewModel.setTags(event.getTags());
    }
    return viewModel;
  }

  public List<EventViewModel> fromEvent(Collection<Event> eventCollection) {

    final List<EventViewModel> eventList = new ArrayList<>(20);
    for (Event event : eventCollection) {
      final EventViewModel viewModel = fromEvent(event);
      if (viewModel != null) {
        eventList.add(viewModel);
      }
    }
    return eventList;
  }

}
