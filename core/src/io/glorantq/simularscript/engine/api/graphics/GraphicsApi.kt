package io.glorantq.simularscript.engine.api.graphics

import com.badlogic.gdx.Gdx
import io.glorantq.simularscript.SSEngine
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * Lua API wrapper for graphics functions
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class GraphicsApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("getFPS", GetFPS())
        library.set("getDeltaTime", GetDeltaTime())
        library.set("getGLVersion", GetGLVersion())
        library.set("getWidth", GetWidth())
        library.set("getHeight", GetHeight())
        library.set("makeSprite", MakeSprite())
        env.set("Graphics", library)
        env.set("Gfx", library)

        return library
    }

    private class GetFPS : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(Gdx.graphics.framesPerSecond)
    }

    private class GetDeltaTime : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(Gdx.graphics.deltaTime.toDouble())
    }

    private class GetGLVersion : ZeroArgFunction() {
        override fun call(): LuaValue = GLVersionTable(Gdx.graphics.glVersion)
    }

    private class GetWidth : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(SSEngine.INSTANCE.window.width)
    }

    private class GetHeight : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(SSEngine.INSTANCE.window.height)
    }

    private class MakeSprite : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue = LuaSprite(arg.tojstring())
    }
}