package io.glorantq.simularscript.engine.api.io

import com.badlogic.gdx.Files
import com.badlogic.gdx.Gdx
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

/**
 * Created by Gerber Lóránt on 2017. 10. 17..
 */
class FileApi : TwoArgFunction() {
    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("getInternal", GetHandle(Files.FileType.Internal))
        library.set("getExternal", GetHandle(Files.FileType.External))
        library.set("getAbsolute", GetHandle(Files.FileType.Absolute))
        library.set("getClasspath", GetHandle(Files.FileType.Classpath))
        library.set("getLocal", GetHandle(Files.FileType.Local))
        env.set("FileIO", library)
        return library
    }

    private class GetHandle(private val type: Files.FileType) : OneArgFunction() {
        override fun call(arg: LuaValue): LuaValue = LuaFile(Gdx.files.getFileHandle(arg.checkjstring(), type))
    }
}