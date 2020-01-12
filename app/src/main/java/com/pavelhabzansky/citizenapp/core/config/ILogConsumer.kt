package com.pavelhabzansky.citizenapp.core.config

interface ILogConsumer {

    fun log(level: Int, TAG: String?, msg: String, ex: String? = null)

}