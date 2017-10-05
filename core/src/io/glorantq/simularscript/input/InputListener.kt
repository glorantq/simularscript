package io.glorantq.simularscript.input

/**
 * Created by Gerber Lóránt on 2017. 10. 02..
 */
interface InputListener {
    fun tap(x: Float, y: Float, button: Int) {

    }

    fun longPress(x: Float, y: Float) {

    }
}