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
 * Screen compiling and running the game scripts
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param sourceFolder Folder to search for the scripts in
 * @param mainScript Name of the main script
 * @param scriptFiles Names of the files to load
 */

class GameScreen(private val sourceFolder: String, private val mainScript: String, private val scriptFiles: List<String>) : KtxScreen {
    private val logger: Logger = ssLogger<GameScreen>()

    /**
     * Map of all the compiled scripts
     */
    val scripts: HashMap<String, LuaScript> = HashMap()

    /**
     * Array of all the APIs provided by the engine
     */
    val includes: Array<String> = arrayOf(
            "io.glorantq.simularscript.engine.api.LogApi",
            "io.glorantq.simularscript.engine.api.multifile.MultifileApi",
            "io.glorantq.simularscript.engine.api.graphics.GraőhicsApi",
            "io.glorantq.simularscript.engine.api.MathApi",
            "io.glorantq.simularscript.engine.api.GameApi",
            "io.glorantq.simularscript.engine.api.CameraApi",
            "io.glorantq.simularscript.engine.api.InputApi",
            "io.glorantq.simularscript.engine.api.io.FileApi"
    )

    /**
     * Entry point of the main script
     */
    private lateinit var mainFunction: LuaFunction

    /**
     * Render function of the main script
     */
    private lateinit var renderFunction: LuaFunction

    fun start() {
        logger.info { "Compiling scripts..." }
        for(name: String in scriptFiles) {
            val handle: FileHandle = Gdx.files.internal("$sourceFolder/$name.lua")
            if(scripts.containsKey(handle.nameWithoutExtension())) {
                throw ScriptingException("Multiple files with the same name compiled!")
            }
            scripts.put(handle.nameWithoutExtension(), LuaScript(handle))
            logger.debug { "Compiled ${handle.nameWithoutExtension()}" }
        }

        if(!scripts.containsKey(mainScript)) {
            throw ScriptingException("Main script not found!")
        }

        val mainScript: LuaScript = scripts[mainScript]!!
        logger.debug { "Main script: $mainScript" }

        if(mainScript.context["ss_main"] == LuaValue.NIL) {
            throw ScriptingException("Main method not found in main script!")
        } else {
            mainFunction = mainScript.context["ss_main"] as LuaFunction

            logger.debug { "Main function: $mainFunction" }
        }

        if(mainScript.context["ss_render"] == LuaValue.NIL) {
            throw ScriptingException("Render function not found in main script!")
        } else {
            renderFunction = mainScript.context["ss_render"] as LuaFunction

            logger.debug { "Render function: $renderFunction" }
        }

        logger.info { "Starting game!" }
        mainFunction.call()
    }

    override fun render(delta: Float) {
        renderFunction.call()
    }
}