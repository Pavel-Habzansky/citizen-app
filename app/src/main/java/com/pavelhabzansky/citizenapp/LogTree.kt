package com.pavelhabzansky.citizenapp

import android.util.Log
import com.pavelhabzansky.citizenapp.core.config.ILogConsumer
import timber.log.Timber

class LogTree(val logConsumer: ILogConsumer) : Timber.Tree() {


    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority > android.util.Log.DEBUG) {
            logConsumer.log(priority, tag, message, t?.toString())
        }

        if (priority >= Log.ERROR) {
            throw UnsupportedOperationException(message)
        }
    }

}