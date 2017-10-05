package io.glorantq.simularscript.input

import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.utils.ssLogger
import ktx.log.Logger

/**
 * Created by Gerber Lóránt on 2017. 10. 02..
 */
class GestureHandler private constructor(): GestureDetector.GestureListener {
    private object Singleton {
        val INSTANCE: GestureHandler = GestureHandler()
    }

    companion object {
        val INSTANCE: GestureHandler by lazy { Singleton.INSTANCE }

        private val logger: Logger = ssLogger<GestureHandler>()

        private val listeners: ArrayList<InputListener> = ArrayList()
        private val toRemove: ArrayList<InputListener> = ArrayList()
        private val toAdd: ArrayList<InputListener> = ArrayList()

        fun register(listener: InputListener) {
            toAdd.add(listener)
            logger.debug { "Registered InputListener ${listener::class.simpleName}" }
        }

        fun remove(listener: InputListener) {
            toRemove.add(listener)
            logger.debug { "Removed InputListener ${listener::class.simpleName}" }
        }

        fun update() {
            listeners.removeAll(toRemove)
            toRemove.clear()
            listeners.addAll(toAdd)
            toAdd.clear()
        }
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        try {
            val realPos: Vector2 = unproject(x, y)

            for (listener in listeners) {
                listener.tap(realPos.x, realPos.y, button)
            }
        } catch (e: Exception) {
            SSEngine.INSTANCE.crash(e)
            listeners.clear()
        }

        return true
    }

    override fun longPress(x: Float, y: Float): Boolean {
        try {
            val realPos: Vector2 = unproject(x, y)

            for (listener in listeners) {
                listener.longPress(realPos.x, realPos.y)
            }
        } catch (e: Exception) {
            SSEngine.INSTANCE.crash(e)
            listeners.clear()
        }

        return true
    }

    private fun unproject(x: Float, y: Float): Vector2 = SSEngine.INSTANCE.viewport.unproject(Vector2(x, y))

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int): Boolean = false
    override fun pinch(initialPointer1: Vector2?, initialPointer2: Vector2?, pointer1: Vector2?, pointer2: Vector2?): Boolean = false
    override fun fling(velocityX: Float, velocityY: Float, button: Int): Boolean = false
    override fun zoom(initialDistance: Float, distance: Float): Boolean = false
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean = false
    override fun pinchStop() = Unit
    override fun panStop(x: Float, y: Float, pointer: Int, button: Int): Boolean = false
}