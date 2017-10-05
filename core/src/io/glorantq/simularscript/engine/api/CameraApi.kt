package io.glorantq.simularscript.engine.api

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.SSEngine
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 03..
 */
class CameraApi : TwoArgFunction() {
    companion object {
        private val camera: OrthographicCamera by lazy { SSEngine.INSTANCE.camera }
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("getX", GetX())
        library.set("getY", GetY())
        library.set("getPosition", GetPosition())
        library.set("setX", SetX())
        library.set("setY", SetY())
        library.set("setPosition", SetPosition())
        library.set("rotate", Rotate())
        library.set("getRotation", GetRotation())
        library.set("getBaseRotation", GetBaseRotation())
        env.set("Camera", library)
        env.set("Cam", library)
        return library
    }

    private class SetX : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            camera.position.x = arg.checkdouble().toFloat()
            return LuaValue.NONE
        }
    }

    private class SetY : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            camera.position.y = arg.checkdouble().toFloat()
            return LuaValue.NONE
        }
    }

    private class SetPosition : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if (arg !is LuaVector2) {
                throw LuaError("Expected LuaVector2")
            }

            camera.position.x = arg.x.toFloat()
            camera.position.y = arg.y.toFloat()

            return LuaValue.NONE
        }
    }

    private class GetX : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(camera.position.x.toDouble())
    }

    private class GetY : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(camera.position.y.toDouble())
    }

    private class GetPosition : ZeroArgFunction() {
        override fun call(): LuaValue = LuaVector2(Vector2(camera.position.x, camera.position.y))
    }

    private class Rotate : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            var deg: Double = arg.checkdouble()
            if(deg > 360) {
                deg -= 360
            }

            if(deg < 0) {
                deg = 360 - deg
            }

            camera.up.set(0f, 1f, 0f)
            camera.direction.set(0f, 0f, -1f)
            camera.rotate(-deg.toFloat() - 180f)
            camera.update()

            return LuaValue.NONE
        }
    }

    private class GetRotation : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(360 - (Math.atan2(camera.up.x.toDouble(), camera.up.y.toDouble()) * MathUtils.radiansToDegrees + 180))
    }

    private class GetBaseRotation : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(180.0)
    }
}