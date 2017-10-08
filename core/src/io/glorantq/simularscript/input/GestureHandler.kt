package io.glorantq.simularscript.input

import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.utils.ssLogger
import ktx.log.Logger

/**
 * Class listening and forwarding gesture events
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class GestureHandler private constructor(): GestureDetector.GestureListener {
    private object Singleton {
        val INSTANCE: GestureHandler = GestureHandler()
    }

    companion object {
        val INSTANCE: GestureHandler by lazy { Singleton.INSTANCE }

        private val listeners: ArrayList<InputListener> = ArrayList()

        fun register(listener: InputListener) {
            synchronized(listeners) {
                listeners.add(listener)
            }
        }

        fun remove(listener: InputListener) {
            synchronized(listeners) {
                listeners.remove(listener)
            }
        }
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        try {
            val realPos: Vector2 = unproject(x, y)

            synchronized(listeners) {
                for (listener in listeners) {
                    if (listener.tap(realPos.x, realPos.y, button)) {
                        break
                    }
                }
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

            synchronized(listeners) {
                for (listener in listeners) {
                    if (listener.longPress(realPos.x, realPos.y)) {
                        break
                    }
                }
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