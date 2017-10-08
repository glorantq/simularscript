package io.glorantq.simularscript.engine.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * API for querying input devices
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class InputApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()

        env.set("Input", library)
        return library
    }
}