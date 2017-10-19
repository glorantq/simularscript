package io.glorantq.simularscript.engine.api

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.utils.SSMath
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * API for querying input devices
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class InputApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("getMousePosition", GetMousePosition())
        library.set("getMouseX", GetMouseX())
        library.set("getMouseY", GetMouseY())
        library.set("isKeyPressed", IsKeyPressed())
        library.set("isKeyJustPressed", IsKeyJustPressed())
        env.set("Input", library)
        return library
    }

    private class GetMousePosition : ZeroArgFunction() {
        override fun call(): LuaValue = LuaVector2(SSMath.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())))
    }

    private class GetMouseX : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(SSMath.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).x.toDouble())
    }

    private class GetMouseY : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(SSMath.unproject(Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())).y.toDouble())
    }

    private class IsKeyPressed : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            val char: String = if(arg.checkstring().tojstring().isEmpty()) { throw LuaError("Provide a key") } else { arg.checkstring().tojstring() }
            return LuaValue.valueOf(Gdx.input.isKeyPressed(Input.Keys.valueOf(char.toUpperCase())))
        }
    }

    private class IsKeyJustPressed : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            val char: String = if(arg.checkstring().tojstring().isEmpty()) { throw LuaError("Provide a key") } else { arg.checkstring().tojstring() }

            return LuaValue.valueOf(Gdx.input.isKeyJustPressed(Input.Keys.valueOf(char.toUpperCase())))
        }
    }
}