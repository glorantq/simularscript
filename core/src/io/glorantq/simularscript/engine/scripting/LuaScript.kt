package io.glorantq.simularscript.engine.scripting

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Version
import com.badlogic.gdx.files.FileHandle
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.utils.ScriptingException
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Prototype
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.jme.JmePlatform
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class LuaScript(val script: FileHandle, vararg includes: String) {
    val context: LuaValue = if(Gdx.app.type == Application.ApplicationType.Android) {
        JmePlatform.standardGlobals()
    } else {
        JsePlatform.standardGlobals()
    }

    val luaCode: Prototype

    init {
        if(!script.exists()) {
            throw ScriptingException("Script doesn't exist")
        }

        includes.forEach { lib ->
            val line = "require \"$lib\""
            val requirement: Prototype = LuaC.instance.compile(line.byteInputStream(Charsets.UTF_8), "script-${script.nameWithoutExtension()}")
            val requirementClosure = LuaClosure(requirement, context)
            requirementClosure.call()
        }

        context.set("_SS_VERSION", SSEngine.VERSION)
        context.set("_GDX_VERSION", Version.VERSION)

        luaCode = LuaC.instance.compile(script.readString("UTF-8").byteInputStream(), "script-${script.nameWithoutExtension()}")
        LuaClosure(luaCode, context).call()
    }
}