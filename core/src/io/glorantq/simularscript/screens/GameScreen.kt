package io.glorantq.simularscript.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import io.glorantq.simularscript.engine.scripting.LuaScript
import io.glorantq.simularscript.utils.ScriptingException
import io.glorantq.simularscript.utils.ssLogger
import ktx.app.KtxScreen
import ktx.log.Logger
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class GameScreen(private val sourceFolder: String, private val mainScript: String, private val scriptFiles: List<String>) : KtxScreen {
    private val logger: Logger = ssLogger<GameScreen>()

    val scripts: HashMap<String, LuaScript> = HashMap()
    val includes: Array<String> = arrayOf(
            "io.glorantq.simularscript.engine.api.LogApi",
            "io.glorantq.simularscript.engine.api.multifile.MultifileApi",
            "io.glorantq.simularscript.engine.api.graphics.GraőhicsApi",
            "io.glorantq.simularscript.engine.api.MathApi",
            "io.glorantq.simularscript.engine.api.GameApi",
            "io.glorantq.simularscript.engine.api.CameraApi"
    )

    private lateinit var mainFunction: LuaFunction
    private lateinit var renderFunction: LuaFunction

    fun start() {
        logger.info { "Compiling scripts..." }
        for(name: String in scriptFiles) {
            val handle: FileHandle = Gdx.files.internal("$sourceFolder/$name.lua")
            scripts.put(handle.nameWithoutExtension(), LuaScript(handle))
            logger.debug { "Compiled ${handle.nameWithoutExtension()}" }
        }

        if(!scripts.containsKey(mainScript)) {
            throw ScriptingException("Main script not found!")
        }

        val mainScript: LuaScript = scripts[mainScript]!!
        if(mainScript.context["ss_main"] == LuaValue.NIL) {
            throw ScriptingException("Main method not found in main script!")
        } else {
            mainFunction = mainScript.context["ss_main"] as LuaFunction
        }

        if(mainScript.context["ss_render"] == LuaValue.NIL) {
            throw ScriptingException("Render function not found in main script!")
        } else {
            renderFunction = mainScript.context["ss_render"] as LuaFunction
        }

        mainFunction.call()
    }

    override fun render(delta: Float) {
        renderFunction.call()
    }
}