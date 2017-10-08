package io.glorantq.simularscript.input

/**
 * Interface to listen to gesture events
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
interface InputListener {
    fun tap(x: Float, y: Float, button: Int): Boolean = false
    fun longPress(x: Float, y: Float): Boolean = false
}