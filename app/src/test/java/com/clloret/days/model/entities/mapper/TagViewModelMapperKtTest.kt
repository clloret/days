package com.clloret.days.model.entities.mapper

import com.clloret.days.domain.entities.Tag
import com.clloret.days.model.entities.TagViewModel
import com.google.common.truth.Truth
import org.junit.Test

class TagViewModelMapperKtTest {

  @Test
  fun toTagViewModel_Always_MapAllValues() {
    val event = buildTag()
    val viewModel = event.toTagViewModel()
    assertEventViewModel(viewModel, event)
  }

  @Test
  fun toTag_Always_MapAllValues() {
    val viewModel = buildTagViewModel()
    val tag = viewModel.toTag()
    assertEvent(tag, viewModel)
  }

  @Test
  fun toTagViewModelList_Always_MapAllValues() {
    val tag = buildTag()
    val tagList = arrayListOf(tag, tag)
    val toTagViewModelList = tagList.toTagViewModelList()

    toTagViewModelList.forEach {
      assertEventViewModel(it, tag)
    }
  }

  private fun buildTag(): Tag {

    return Tag(
            id = "id",
            name = "name")
  }

  private fun buildTagViewModel(): TagViewModel {

    return TagViewModel(
            id = "id",
            name = "name")
  }

  private fun assertEventViewModel(vm: TagViewModel, event: Tag) {
    Truth.assertThat(vm.id).isEqualTo(event.id)
    Truth.assertThat(vm.name).isEqualTo(event.name)
  }

  private fun assertEvent(event: Tag, vm: TagViewModel) {
    Truth.assertThat(event.id).isEqualTo(vm.id)
    Truth.assertThat(event.name).isEqualTo(vm.name)
  }

}