package io.glorantq.simularscript.engine

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class Window(val width: Int, val height: Int, val fullscreen: Boolean, val title: String) {
    override fun toString(): String = ToStringBuilder.reflectionToString(this)
}