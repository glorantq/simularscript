package io.glorantq.simularscript.engine.api

import com.badlogic.gdx.math.Vector2
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 02..
 */
class LuaVector2(val x: Double, val y: Double) : LuaTable() {
    constructor(vector: Vector2): this(vector.x.toDouble(), vector.y.toDouble())

    private var backingVector: Vector2 = Vector2(x.toFloat(), y.toFloat())

    init {
        set("x", x)
        set("y", y)

        set("add", Vec2Add(this))
        set("sub", Vec2Sub(this))
    }

    private class Vec2Add(private val vec1: LuaVector2) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if(arg !is LuaVector2) {
                typerror("vector2")
                return LuaValue.NONE
            }

            val vec2: Vector2 = arg.backingVector

            return LuaVector2(vec1.backingVector.add(vec2))
        }
    }

    private class Vec2Sub(private val vec1: LuaVector2) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if(arg !is LuaVector2) {
                typerror("vector2")
                return LuaValue.NONE
            }

            val vec2: Vector2 = arg.backingVector

            return LuaVector2(vec1.backingVector.sub(vec2))
        }
    }

    override fun tostring(): LuaValue = LuaValue.valueOf("Vector2($x, $y)")
    override fun tojstring(): String = tostring().tojstring()
}