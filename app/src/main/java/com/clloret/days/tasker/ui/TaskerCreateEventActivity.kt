package com.clloret.days.tasker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import butterknife.ButterKnife
import com.clloret.days.R
import com.twofortyfouram.locale.sdk.client.ui.activity.AbstractAppCompatPluginActivity

class TaskerCreateEventActivity : AbstractAppCompatPluginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasker_create_event)

        ButterKnife.bind(this)
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
            R.id.menu_delete -> {
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

    override fun onPostCreateWithPreviousResult(bundle: Bundle, previousBlurb: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultBundle(): Bundle? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isBundleValid(bundle: Bundle): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getResultBlurb(bundle: Bundle): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
