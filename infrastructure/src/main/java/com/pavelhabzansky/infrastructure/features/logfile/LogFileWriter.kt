package com.pavelhabzansky.infrastructure.features.logfile

import java.io.*

class LogFileWriter(val logFile: File, val tmpFile: File) {

    @Volatile
    private var isLogFileLocked = true

    private val logBuffer = FiFoList<String>()

    init {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
                println("Can not create log file! Exception: $e")
            }
        }

        isLogFileLocked = false
        println("Write logs to file: ${this.logFile.absolutePath}")
    }

    fun appendLog(msg: String) {
        try {

            logBuffer.add(msg)
            if (!isLogFileLocked) {
                writeLogAsyncToFile()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            println("Can not append to log file! Exception: $e")
        }
    }

    private fun writeLogAsyncToFile() {
        isLogFileLocked = true
        if (logFile.length() > MAX_FILE_SIZE) {
            //println(" LOGFILE IS TOO LONG. SIZE: " + logFile.length() / 1024 + " kB / max = $MAX_FILE_SIZE_KB kB")
            removeLines()
            writeBufferToFile()
        } else {
            writeBufferToFile()
        }

        //try {
        //    println("log file size = " + logFile.length() / 1024 + " kB // max = $MAX_FILE_SIZE_KB kB")
        //} catch (e: Exception) {
        //    println("Can not log the result. Exception: " + e)
        //}
        isLogFileLocked = false
    }

    private fun writeBufferToFile() {

        try {
            //BufferedWriter for performance, true to set append to file flag
            val buf = BufferedWriter(FileWriter(logFile, true))
            var line = logBuffer.get();
            while (line != null) {
                buf.append(line)
                buf.newLine()
                line = logBuffer.get()
            }
            buf.flush()
            buf.close()
        } catch (e: IOException) {
            e.printStackTrace()
            println("Can not write to log file! log buffer = ${logBuffer} Exception: $e")
        }
    }

    private fun removeLines(): Boolean {
        try {
            //println("BEGIN DELETING THE LINES FROM THE LOGFILE")

            if (!logFile.isFile || !logFile.exists()) {
                println("Parameter is not an existing logfile")
                return false
            }

            // Construct the new file that will later be renamed to the original
            // filename.
            //File tempFile = App.getFile(TMP_FILE_NAME);
            if (!tmpFile.exists()) {
                try {
                    //println("create tmp logfile: " + tmpFile.absolutePath)
                    tmpFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    println("Can not create the TMP logfile...")
                    return false
                }

            }

            val br = BufferedReader(FileReader(logFile))
            val pw = PrintWriter(FileWriter(tmpFile))

            var line: String? = null
            var total: Long = 0
            val delete = MAX_FILE_SIZE * 2 / 3
            var allLines = 0
            var deletedLines = 0

            // Read from the original file and write to the new one
            // unless content matches data to be removed.
            while ({ line = br.readLine(); line }() != null) {
                total += line!!.length.toLong()
                ++allLines
                if (total > delete) {
                    pw.println(line)
                    pw.flush()
                    //buf.append(line);
                    //buf.newLine();
                    //buf.flush();
                } else {
                    ++deletedLines
                }
            }
            pw.close()
            //buf.close();
            br.close()

            //println("DELETED logfile lines: $deletedLines / $allLines lines; deleted string length = $trueDeleted / $total")

            // Delete the original file
            if (!logFile.delete()) {
                println("Could not delete logfile")
                return false
            }

            // Rename the new file to the filename the original file had.
            if (!tmpFile.renameTo(logFile)) {
                println("Could not rename file")
                return false
            }


            try {
                println("END DELETING THE LINES FROM THE LOGFILE; New file size = ${logFile.length() / KB} kB")
            } catch (e: Exception) {
                println("Can not log the result. Exception: $e")
            }

            return true

        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return false
    }

    companion object {
        val KB = 1024
        val MAX_FILE_SIZE_KB = 800L
        val MAX_FILE_SIZE = MAX_FILE_SIZE_KB * KB
    }

}