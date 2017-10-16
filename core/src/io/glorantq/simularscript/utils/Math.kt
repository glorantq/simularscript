package io.glorantq.simularscript.utils

import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.SSEngine

/**
 * Created by Gerber Lóránt on 2017. 10. 16..
 */

object SSMath {
    fun clamp(max: Number, min: Number, number: Number): Number = Math.min(Math.max(min.toDouble(), number.toDouble()), max.toDouble())
    fun unproject(coords: Vector2): Vector2 = SSEngine.INSTANCE.viewport.unproject(coords)
}