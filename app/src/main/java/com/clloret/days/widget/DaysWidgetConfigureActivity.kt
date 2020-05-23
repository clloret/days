package com.clloret.days.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.clloret.days.R
import com.clloret.days.model.entities.EventViewModel
import com.clloret.days.tasker.ui.TaskerEditEventActivity
import com.clloret.days.tasker.ui.TaskerSelectEventActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_tasker_edit_event.btnSelectEvent
import kotlinx.android.synthetic.main.days_widget_configure.*
import javax.inject.Inject

/**
 * The configuration screen for the [DaysWidget] AppWidget.
 */
class DaysWidgetConfigureActivity : AppCompatActivity() {

  private var eventId: String? = null
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  @Inject
  lateinit var updateAppWidget: UpdateAppWidget

  public override fun onCreate(icicle: Bundle?) {
    super.onCreate(icicle)

    AndroidInjection.inject(this)

    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED)

    setContentView(R.layout.days_widget_configure)

    btnSelectEvent.setOnClickListener {
      val intent = Intent(this, TaskerSelectEventActivity::class.java)
      startActivityForResult(intent, TaskerEditEventActivity.REQUEST_CODE_SELECT_EVENT)
    }

    btnAddWidget.setOnClickListener {
      val context = this

      if (eventId == null) {
        showAlertDialog(context)
      }
      val something = eventId ?: return@setOnClickListener

      DaysWidgetPrefs.saveEventIdPref(context, appWidgetId, something)

      // It is the responsibility of the configuration activity to update the app widget
      val appWidgetManager = AppWidgetManager.getInstance(context)
      updateAppWidget.update(context, appWidgetManager, appWidgetId)

      // Make sure we pass back the original appWidgetId
      val resultValue = Intent()
      resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      setResult(RESULT_OK, resultValue)
      finish()
    }

    // Find the widget id from the intent.
    val intent = intent
    val extras = intent.extras
    if (extras != null) {
      appWidgetId = extras.getInt(
              AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
    }

    // If this activity was started with an intent without an app widget ID, finish with an error.
    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    val savedEventId = DaysWidgetPrefs.loadEventIdPref(this, appWidgetId)
    val eventName = savedEventId?.let { "Event ID: $it" } ?: "Event Name"
    textViewEventName.text = eventName

  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == TaskerEditEventActivity.REQUEST_CODE_SELECT_EVENT) {
      if (resultCode == Activity.RESULT_OK) {
        val selectedEvent: EventViewModel? = data?.getParcelableExtra(TaskerSelectEventActivity.EXTRA_EVENT)
        eventId = selectedEvent?.id
        textViewEventName.text = selectedEvent?.name
      }
    }
  }

}

internal fun showAlertDialog(context: Context) {
  AlertDialog.Builder(context).setTitle(R.string.title_warning)
          .setMessage(R.string.msg_error_you_must_select_an_event)
          .setIcon(android.R.drawable.ic_dialog_alert)
          .setPositiveButton(R.string.action_ok, null)
          .show()
}