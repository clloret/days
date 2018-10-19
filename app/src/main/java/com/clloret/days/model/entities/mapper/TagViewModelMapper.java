package com.clloret.days.model.entities.mapper;

import com.clloret.days.domain.entities.Tag;
import com.clloret.days.model.entities.TagViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TagViewModelMapper {

  public Tag toTag(TagViewModel viewModel) {

    Tag tag = null;
    if (viewModel != null) {
      tag = new Tag(viewModel.getId());
      tag.setName(viewModel.getName());
    }
    return tag;
  }

  public TagViewModel fromTag(Tag tag) {

    TagViewModel viewModel = null;
    if (tag != null) {
      viewModel = new TagViewModel(tag.getId());
      viewModel.setName(tag.getName());
    }
    return viewModel;
  }

  public List<TagViewModel> fromTag(Collection<Tag> tagCollection) {

    final List<TagViewModel> viewModels = new ArrayList<>(20);
    for (Tag tag : tagCollection) {
      final TagViewModel viewModel = fromTag(tag);
      if (viewModel != null) {
        viewModels.add(viewModel);
      }
    }
    return viewModels;
  }

}
