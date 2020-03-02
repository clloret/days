package com.clloret.days.tasker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.clloret.days.R
import com.clloret.days.tasker.bundle.EventCreateBundle
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity
import kotlinx.android.synthetic.main.activity_tasker_create_event.*
import net.dinglisch.android.tasker.TaskerPlugin
import timber.log.Timber

class TaskerCreateEventActivity : AbstractAppCompatPluginActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_tasker_create_event)

  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_tasker_create_event, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {

    return when (item?.itemId) {
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
    finish()
  }

  override fun onPostCreateWithPreviousResult(previousBundle: Bundle, previousBlurb: String) {
    Timber.d("onPostCreateWithPreviousResult")

    val bundle = EventCreateBundle(previousBundle)
    name.setText(bundle.title)
    description.setText(bundle.description)
    date.setText(bundle.date)
    reminder.setText(bundle.reminder)
  }

  override fun getResultBundle(): Bundle? {
    Timber.d("getResultBundle")

    val bundle = EventCreateBundle()
    bundle.title = name.text.toString().trim()
    bundle.description = description.text.toString().trim()
    bundle.date = date.text.toString().trim()
    bundle.reminder = reminder.text.toString().trim()

    val resultBundle: Bundle = bundle.build()
    if (TaskerPlugin.Setting.hostSupportsOnFireVariableReplacement(this)) {
      TaskerPlugin.Setting.setVariableReplaceKeys(
              resultBundle, arrayOf(
              EventCreateBundle.EXTRA_NAME,
              EventCreateBundle.EXTRA_DESCRIPTION,
              EventCreateBundle.EXTRA_DATE,
              EventCreateBundle.EXTRA_REMINDER
      ))
    }
    return resultBundle
  }

  override fun isBundleValid(bundle: Bundle): Boolean {
    return EventCreateBundle.isBundleValid(bundle)
  }

  override fun getResultBlurb(bundle: Bundle): String {
    return name.text.toString().trim()
  }

}
