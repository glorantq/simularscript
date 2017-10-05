package io.glorantq.simularscript.engine.api.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.engine.api.LuaVector2
import io.glorantq.simularscript.input.GestureHandler
import io.glorantq.simularscript.input.InputListener
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 02..
 */

class LuaSprite(path: String) : LuaTable(), InputListener {
    companion object {
        private val engine: SSEngine = SSEngine.INSTANCE
    }

    private val backingSprite: Sprite
    private var onClickFunction: LuaValue = LuaValue.NONE
    private var longClickFunction: LuaValue = LuaValue.NONE

    init {
        val handle: FileHandle = Gdx.files.internal(path)
        backingSprite = Sprite(Texture(handle))

        set("draw", Draw(this))

        set("setX", SetX(this))
        set("setY", SetY(this))
        set("setPosition", SetPosition(this))

        set("getX", GetX(this))
        set("getY", GetY(this))
        set("getPosition", GetPosition(this))

        set("setWidth", SetWidth(this))
        set("setHeight", SetHeight(this))
        set("setSize", SetSize(this))

        set("getWidth", GetWidth(this))
        set("getHeight", GetHeight(this))
        set("getSize", GetSize(this))

        set("dispose", Dispose(this))

        GestureHandler.register(this)
    }

    override fun tap(x: Float, y: Float, button: Int) {
        if (onClickFunction !is LuaFunction) return
        if (backingSprite.boundingRectangle.contains(x, y)) {
            onClickFunction.call(LuaValue.valueOf(button))
        }
    }

    override fun longPress(x: Float, y: Float) {
        if(longClickFunction !is LuaFunction) return
        if (backingSprite.boundingRectangle.contains(x, y)) {
            longClickFunction.call()
        }
    }

    override fun set(key: LuaValue, value: LuaValue) {
        when (key.tojstring()) {
            "clickHandler" -> onClickFunction = value
            "longClickHandler" -> longClickFunction = value
            else -> super.set(key, value)
        }
    }

    private class Draw(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue {
            sprite.backingSprite.draw(engine.spriteBatch)
            return LuaValue.NONE
        }
    }

    private class SetX(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            sprite.backingSprite.x = arg.checkdouble().toFloat()
            return LuaValue.NONE
        }
    }

    private class SetY(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            sprite.backingSprite.y = arg.checkdouble().toFloat()
            return LuaValue.NONE
        }
    }

    private class SetPosition(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if (arg !is LuaVector2) {
                throw LuaError("Expected LuaVector2")
            }

            sprite.backingSprite.setPosition(arg.x.toFloat(), arg.y.toFloat())

            return LuaValue.NONE
        }
    }

    private class GetX(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(sprite.backingSprite.x.toDouble())
    }

    private class GetY(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(sprite.backingSprite.y.toDouble())
    }

    private class GetPosition(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaVector2(Vector2(sprite.backingSprite.x, sprite.backingSprite.y))
    }

    private class SetWidth(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            sprite.backingSprite.setSize(arg.checkdouble().toFloat(), sprite.backingSprite.y)

            return LuaValue.NONE
        }
    }

    private class SetHeight(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            sprite.backingSprite.setSize(sprite.backingSprite.x, arg.checkdouble().toFloat())

            return LuaValue.NONE
        }
    }

    private class SetSize(private val sprite: LuaSprite) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue {
            if (arg !is LuaVector2) {
                throw LuaError("Expected LuaVector2")
            }

            sprite.backingSprite.setSize(arg.x.toFloat(), arg.y.toFloat())

            return LuaValue.NONE
        }
    }

    private class GetWidth(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(sprite.backingSprite.width.toDouble())
    }

    private class GetHeight(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(sprite.backingSprite.height.toDouble())
    }

    private class GetSize(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaVector2(Vector2(sprite.backingSprite.width, sprite.backingSprite.height))
    }

    private class Dispose(private val sprite: LuaSprite) : ZeroArgFunction() {
        override fun call(): LuaValue {
            GestureHandler.remove(sprite)

            return LuaValue.NONE
        }
    }
}