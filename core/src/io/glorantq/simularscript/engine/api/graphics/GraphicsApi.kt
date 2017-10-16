package io.glorantq.simularscript.engine.api.graphics

import com.badlogic.gdx.Gdx
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.utils.SSMath
import io.glorantq.simularscript.utils.rgbToOGL
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ThreeArgFunction
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
        library.set("setFullscreen", SetFullscreen())
        library.set("toggleFullscreen", ToggleFullscreen())
        library.set("setClearColour", ClearColour())
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

    private class SetFullscreen : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if(arg.checkboolean()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            } else {
                Gdx.graphics.setWindowedMode(SSEngine.WIDTH.toInt(), SSEngine.HEIGHT.toInt())
            }

            return LuaValue.NONE
        }
    }

    private class ToggleFullscreen : ZeroArgFunction() {
        override fun call(): LuaValue {
            if(Gdx.graphics.isFullscreen) {
                Gdx.graphics.setWindowedMode(SSEngine.WIDTH.toInt(), SSEngine.HEIGHT.toInt())
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            }

            return LuaValue.NONE
        }
    }

    private class ClearColour : ThreeArgFunction() {
        override fun call(arg1: LuaValue, arg2: LuaValue, arg3: LuaValue): LuaValue {
            val r: Float = SSMath.clamp(255, 0, arg1.checkint()).toInt().rgbToOGL()
            val g: Float = SSMath.clamp(255, 0, arg2.checkint()).toInt().rgbToOGL()
            val b: Float = SSMath.clamp(255, 0, arg3.checkint()).toInt().rgbToOGL()

            SSEngine.INSTANCE.setBaseColour(r, g, b)
            return LuaValue.NONE
        }
    }
}