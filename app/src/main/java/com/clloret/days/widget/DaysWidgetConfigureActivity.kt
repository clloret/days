package com.clloret.days.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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

  @Inject
  lateinit var updateAppWidget: UpdateAppWidget

  private var eventId: String? = null
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  private var onClickListener = View.OnClickListener {
    val context = this

    // When the button is clicked, store the string locally
    //val widgetText = appWidgetText.text.toString()
    val widgetText = eventId!!
    saveTitlePref(context, appWidgetId, widgetText)

    // It is the responsibility of the configuration activity to update the app widget
    val appWidgetManager = AppWidgetManager.getInstance(context)
//    updateAppWidget(context, appWidgetManager, appWidgetId, getEventUseCase)
    updateAppWidget.update(context, appWidgetManager, appWidgetId)

    // Make sure we pass back the original appWidgetId
    val resultValue = Intent()
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
    setResult(RESULT_OK, resultValue)
    finish()
  }

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

    add_button.setOnClickListener(onClickListener)

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

    appwidget_text.setText(loadTitlePref(this, appWidgetId))
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == TaskerEditEventActivity.REQUEST_CODE_SELECT_EVENT) {
      if (resultCode == Activity.RESULT_OK) {
        val selectedEvent: EventViewModel? = data?.getParcelableExtra(TaskerSelectEventActivity.EXTRA_EVENT)
        eventId = selectedEvent?.id
      }
    }
  }

}

private const val PREFS_NAME = "com.clloret.days.widget.DaysWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

// Write the prefix to the SharedPreferences object for this widget
internal fun saveTitlePref(context: Context, appWidgetId: Int, text: String) {
  val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
  prefs.putString(PREF_PREFIX_KEY + appWidgetId, text)
  prefs.apply()
}

// Read the prefix from the SharedPreferences object for this widget.
// If there is no preference saved, get the default from a resource
internal fun loadTitlePref(context: Context, appWidgetId: Int): String {
  val prefs = context.getSharedPreferences(PREFS_NAME, 0)
  val titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null)
  return titleValue ?: context.getString(R.string.appwidget_text)
}

internal fun deleteTitlePref(context: Context, appWidgetId: Int) {
  val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
  prefs.remove(PREF_PREFIX_KEY + appWidgetId)
  prefs.apply()
}