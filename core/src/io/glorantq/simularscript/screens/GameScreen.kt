package io.glorantq.simularscript.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import io.glorantq.simularscript.engine.scripting.LuaScript
import io.glorantq.simularscript.utils.ScriptingException
import io.glorantq.simularscript.utils.ssLogger
import ktx.app.KtxScreen
import ktx.log.Logger

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class GameScreen(private val sourceFolder: String, private val mainScript: String, private val scriptFiles: List<String>) : KtxScreen {
    private val logger: Logger = ssLogger<GameScreen>()

    val scripts: HashMap<String, LuaScript> = HashMap()
    val includes: Array<String> = arrayOf(
            "io.glorantq.simularscript.engine.api.LogApi"
    )

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

        scripts[mainScript]!!.context["main"].call()
    }
}