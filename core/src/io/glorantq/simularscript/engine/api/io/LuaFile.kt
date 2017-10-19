package io.glorantq.simularscript.engine.api.io

import com.badlogic.gdx.files.FileHandle
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 17..
 */
class LuaFile(val backingHandle: FileHandle) : LuaTable() {
    init {
        set("readString", ReadString(this))
        set("exists", Exists(this))
        set("name", Name(this))
        set("extension", Extension(this))
    }

    private class Exists(private val file: LuaFile) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(file.backingHandle.exists())
    }

    private class ReadString(private val file: LuaFile) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(file.backingHandle.readString("UTF-8"))
    }

    private class Name(private val file: LuaFile) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(file.backingHandle.nameWithoutExtension())
    }

    private class Extension(private val file: LuaFile) : ZeroArgFunction() {
        override fun call(): LuaValue = LuaValue.valueOf(file.backingHandle.extension())
    }
}