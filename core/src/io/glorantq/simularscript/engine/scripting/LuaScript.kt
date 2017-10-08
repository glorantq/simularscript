package io.glorantq.simularscript.engine.scripting

import com.badlogic.gdx.Version
import com.badlogic.gdx.files.FileHandle
import io.glorantq.simularscript.SSEngine
import io.glorantq.simularscript.utils.ScriptingException
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Prototype
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.jse.JsePlatform

/**
 * Object representing a script
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param script FileHandle to the script file
 * @param includes List of modules that should be imported automatically
 */
class LuaScript(val script: FileHandle, vararg includes: String) {

    /**
     * Context of this script
     */
    val context: LuaValue = JsePlatform.standardGlobals()

    /**
     * Compiled code for this script
     */
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

    override fun toString(): String = "LuaScript(context=${context.tojstring()}, script=${script.nameWithoutExtension()})"
}