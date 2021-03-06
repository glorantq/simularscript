package io.glorantq.simularscript.engine.api.multifile

import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.engine.scripting.LuaScript
import io.glorantq.simularscript.screens.GameScreen
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * API for using multiple files in one game
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */

class MultifileApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        val getFile = GetFile()
        library.set("getFile", getFile)
        env.set("__", getFile)
        env.set("Multifile", library)
        return library
    }

    private class GetFile : OneArgFunction() {
        override fun call(fileName: LuaValue): LuaValue {
            val name: String = fileName.tojstring()
            val gameScreen: GameScreen = SSEngine.INSTANCE.gameScreen

            return if(gameScreen.scripts.containsKey(name)) {
                val script: LuaScript = gameScreen.scripts[name]!!
                LuaLinkerTable(script.context, name)
            } else {
                LuaValue.NONE
            }
        }
    }
}