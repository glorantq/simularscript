package io.glorantq.simularscript.engine.api

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 05..
 */
class InputApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()

        env.set("Input", library)
        return library
    }
}