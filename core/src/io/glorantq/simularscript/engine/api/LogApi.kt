package io.glorantq.simularscript.engine.api

import io.glorantq.simularscript.utils.SSLogger
import ktx.log.Logger
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class LogApi : TwoArgFunction() {
    companion object {
        private val logger: Logger = SSLogger("ScriptedGame")
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("info", Info())
        library.set("debug", Debug())
        library.set("error", Error())
        env.set("Log", library)
        return library
    }

    private class Info : OneArgFunction() {
        override fun call(message: LuaValue): LuaValue {
            logger.info { message.tojstring() }
            return LuaValue.NIL
        }
    }

    private class Debug : OneArgFunction() {
        override fun call(message: LuaValue): LuaValue {
            logger.debug { message.tojstring() }
            return LuaValue.NIL
        }
    }

    private class Error : OneArgFunction() {
        override fun call(message: LuaValue): LuaValue {
            logger.error { message.tojstring() }
            return LuaValue.NIL
        }
    }
}