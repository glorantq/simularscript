package io.glorantq.simularscript.engine.api.multifile

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class LuaLinkerTable(private val context: LuaValue, private val filename: String) : LuaTable() {
    override fun get(key: String): LuaValue = context[key]
    override fun get(key: LuaValue?): LuaValue = context.get(key)
    override fun set(key: Int, value: LuaValue?) = Unit
    override fun set(key: LuaValue?, value: LuaValue?) = Unit
    override fun get(key: Int): LuaValue = context.get(key)
    override fun rawget(key: Int): LuaValue = context.rawget(key)
    override fun rawget(key: LuaValue?): LuaValue = context.rawget(key)
    override fun getmetatable(): LuaValue = context.getmetatable()

    override fun tostring(): LuaValue = LuaValue.valueOf("LLT($filename)")
    override fun tojstring(): String = tostring().tojstring()
}