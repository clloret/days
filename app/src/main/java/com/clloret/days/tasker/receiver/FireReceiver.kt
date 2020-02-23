package com.clloret.days.tasker.receiver

import android.content.Context
import android.os.Bundle
import com.twofortyfouram.locale.sdk.client.receiver.AbstractPluginSettingReceiver
import timber.log.Timber

class FireReceiver : AbstractPluginSettingReceiver() {
    override fun isBundleValid(bundle: Bundle): Boolean {
        Timber.d("isBundleValid")

        return true
    }

    override fun isAsync(): Boolean {
        Timber.d("isAsync")

        return false
    }

    override fun firePluginSetting(context: Context, bundle: Bundle) {
        Timber.d("firePluginSetting")

    }
}