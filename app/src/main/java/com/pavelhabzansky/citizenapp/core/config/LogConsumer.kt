package com.pavelhabzansky.citizenapp.core.config

import com.pavelhabzansky.citizenapp.core.LANGUAGE_CZ
import com.pavelhabzansky.infrastructure.features.logfile.LogFileWriter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LogConsumer(filesDir: File) : ILogConsumer {

    companion object {
        val LOG_FILE_NAME = "logfile.txt"
        val TMP_FILE_NAME = "tmpLogFile"

        val DATE_TIME_STAMP_HIRES_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    }

    private val logFileWriter: LogFileWriter
    private val loc = Locale(LANGUAGE_CZ)

    init {
        val logFile = getFile(filesDir, LOG_FILE_NAME)
        val tmpFile = getFile(filesDir, TMP_FILE_NAME)
        logFileWriter = LogFileWriter(logFile, tmpFile)
    }

    override fun log(level: Int, TAG: String?, msg: String, ex: String?) {
        logFileWriter.appendLog("${getTimeStampString()} $level/${TAG ?: ""}; $msg ${ex ?: ""}")
    }

    private fun getTimeStampString(): String {
        val now = Calendar.getInstance()
        val formatter = SimpleDateFormat(DATE_TIME_STAMP_HIRES_FORMAT, loc)
        return formatter.format(now.time)
    }

    private fun getFile(filesDir: File, fileName: String): File {
        return File(filesDir, fileName)
    }

}