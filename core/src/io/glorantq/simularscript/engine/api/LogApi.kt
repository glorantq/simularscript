package io.glorantq.simularscript.engine.api

import io.glorantq.simularscript.utils.ssLogger
import ktx.log.Logger
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Logging API
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class LogApi : TwoArgFunction() {
    companion object {
        private val logger: Logger = ssLogger<LogApi>()
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
            return LuaValue.NONE
        }
    }

    private class Debug : OneArgFunction() {
        override fun call(message: LuaValue): LuaValue {
            logger.debug { message.tojstring() }
            return LuaValue.NONE
        }
    }

    private class Error : OneArgFunction() {
        override fun call(message: LuaValue): LuaValue {
            logger.error { message.tojstring() }
            return LuaValue.NONE
        }
    }
}