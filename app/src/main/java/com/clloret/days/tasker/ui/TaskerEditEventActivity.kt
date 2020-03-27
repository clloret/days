package com.clloret.days.tasker.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.clloret.days.R
import com.clloret.days.model.entities.EventViewModel
import com.clloret.days.tasker.bundle.EventEditBundle
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity
import kotlinx.android.synthetic.main.activity_tasker_edit_event.*
import net.dinglisch.android.tasker.TaskerPlugin
import timber.log.Timber

class TaskerEditEventActivity : AbstractAppCompatPluginActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tasker_edit_event)

    btnSelectEvent.setOnClickListener {
      val intent = Intent(this, TaskerSelectEventActivity::class.java)
      startActivityForResult(intent, REQUEST_CODE_SELECT_EVENT)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == REQUEST_CODE_SELECT_EVENT) {
      if (resultCode == Activity.RESULT_OK) {
        val selectedEvent: EventViewModel? = data?.getParcelableExtra(TaskerSelectEventActivity.EXTRA_EVENT)
        id.setText(selectedEvent?.id)
      }
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_tasker_create_event, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {

    return when (item.itemId) {
      R.id.menu_save -> {
        save()
        true
      }
      R.id.menu_cancel -> {
        discard()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()

    discard()
  }

  private fun discard() {
    mIsCancelled = true
    finish()
  }

  private fun save() {
    if (!validateId()) return

    finish()
  }

  private fun validateId(): Boolean {
    if (id.text.isNullOrEmpty()) {
      idLayout.error = getString(R.string.msg_error_event_id_required)
      idLayout.isErrorEnabled = true
      idLayout.editText?.requestFocus()

      return false
    }
    return true
  }

  override fun onPostCreateWithPreviousResult(previousBundle: Bundle, previousBlurb: String) {
    Timber.d("onPostCreateWithPreviousResult")

    val bundle = EventEditBundle(previousBundle)

    id.setText(bundle.id)
    name.setText(bundle.name)
    description.setText(bundle.description)
    date.setText(bundle.date)
    reminder.setText(bundle.reminder)
    favorite.setText(bundle.favorite)
  }

  private fun getEditTextValue(editText: EditText): String? {
    return if (!editText.text.isNullOrBlank()) editText.text.toString().trim() else null
  }

  override fun getResultBundle(): Bundle? {
    Timber.d("getResultBundle")

    val bundle = EventEditBundle()
    bundle.id = id.text.toString().trim()
    bundle.name = name.text.toString().trim()
    bundle.description = getEditTextValue(description)
    bundle.date = getEditTextValue(date)
    bundle.reminder = getEditTextValue(reminder)
    bundle.favorite = getEditTextValue(favorite)

    val resultBundle: Bundle = bundle.build()
    if (TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement(this)) {
      TaskerPlugin.Setting.setVariableReplaceKeys(
              resultBundle, arrayOf(
              EventEditBundle.EXTRA_NAME,
              EventEditBundle.EXTRA_DESCRIPTION,
              EventEditBundle.EXTRA_DATE,
              EventEditBundle.EXTRA_REMINDER,
              EventEditBundle.EXTRA_FAVORITE
      ))
    }
    return resultBundle
  }

  override fun isBundleValid(bundle: Bundle): Boolean {
    return EventEditBundle.isBundleValid(bundle)
  }

  override fun getResultBlurb(bundle: Bundle): String {
    return "Edit event with ID: ${id.text.toString().trim()}"
  }

  companion object {
    const val REQUEST_CODE_SELECT_EVENT = 1
  }

}
