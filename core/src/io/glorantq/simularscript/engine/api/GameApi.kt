package io.glorantq.simularscript.engine.api

import com.badlogic.gdx.Gdx
import io.glorantq.simularscript.utils.GameException
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.LibFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 02..
 */
class GameApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("close", Close())
        library.set("throw", Throw())
        env.set("Game", library)
        env.set("App", library)
        return library
    }

    private class Close : ZeroArgFunction() {
        override fun call(): LuaValue {
            Gdx.app.exit()
            return LuaValue.NONE
        }
    }

    private class Throw : LibFunction() {
        override fun call(): LuaValue {
            throw GameException()
        }

        override fun call(a: LuaValue): LuaValue {
            throw GameException(a.tojstring())
        }

        override fun call(a: LuaValue, b: LuaValue): LuaValue {
            throw GameException(a.tojstring(), GameException(b.tojstring()))
        }
    }
}