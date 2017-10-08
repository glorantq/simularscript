package io.glorantq.simularscript.engine

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Data class storing information about the game window
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param width Width of the window
 * @param height Height of the window
 * @param fullscreen Should the window start in fullscreen mode?
 * @param title Title of the window
 */
class Window(val width: Int, val height: Int, val fullscreen: Boolean, val title: String) {
    override fun toString(): String = ToStringBuilder.reflectionToString(this)
}