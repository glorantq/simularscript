package io.glorantq.simularscript.engine.api.multifile

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * LuaTable linking to another script context.
 * Redirects ``LuaTable#get()`` and ``LuaTable#set()`` methods to the other context
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param context Context of the other script
 * @param filename Name of the other context (file)
 */

class LuaLinkerTable(private val context: LuaValue, private val filename: String) : LuaTable() {
    override fun get(key: String): LuaValue = context[key]
    override fun get(key: LuaValue?): LuaValue = context.get(key)
    override fun get(key: Int): LuaValue = context.get(key)
    override fun rawget(key: Int): LuaValue = context.rawget(key)
    override fun rawget(key: LuaValue?): LuaValue = context.rawget(key)
    override fun getmetatable(): LuaValue = context.getmetatable()

    override fun set(key: Int, value: LuaValue?) = context.set(key, value)
    override fun set(key: LuaValue?, value: LuaValue?) = context.set(key, value)
    override fun set(key: String?, value: LuaValue?) = context.set(key, value)

    override fun tostring(): LuaValue = LuaValue.valueOf("LLT($filename)")
    override fun tojstring(): String = tostring().tojstring()
}