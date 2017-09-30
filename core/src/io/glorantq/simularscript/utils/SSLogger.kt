package io.glorantq.simularscript.utils

import ktx.log.Logger
import java.util.*

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */

class SSLogger(tag: String) : Logger(tag) {
    override val debugTag: String
        get() = "[DEBUG] ${getFormattedTime()} $tag"
    override val errorTag: String
        get() = "[ERROR] ${getFormattedTime()} $tag"
    override val infoTag: String
        get() = "[INFO] ${getFormattedTime()} $tag"

    private fun getFormattedTime(): String {
        val c: Calendar = Calendar.getInstance()

        val sBuilder = StringBuilder()
        sBuilder.append(String.format(Locale.getDefault(), "%02d", c.get(Calendar.HOUR_OF_DAY)))
        sBuilder.append(":")
        sBuilder.append(String.format(Locale.getDefault(), "%02d", c.get(Calendar.MINUTE)))
        sBuilder.append(":")
        sBuilder.append(String.format(Locale.getDefault(), "%02d", c.get(Calendar.SECOND)))

        return sBuilder.toString()
    }
}

inline fun <reified T : Any> ssLogger(): Logger = SSLogger(T::class.java.name)