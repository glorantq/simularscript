package io.glorantq.simularscript

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import io.glorantq.simularscript.config.JSONConfig
import io.glorantq.simularscript.utils.hexToOGL
import io.glorantq.simularscript.utils.hexToRGB
import io.glorantq.simularscript.utils.rgbToOGL
import io.glorantq.simularscript.utils.ssLogger
import ktx.app.KtxGame
import ktx.app.emptyScreen
import ktx.log.Logger
import java.util.*

class SSEngine private constructor() : KtxGame<Screen>() {
    private object Singleton {
        val INSTANCE: SSEngine = SSEngine()
    }

    companion object {
        val INSTANCE: SSEngine by lazy { Singleton.INSTANCE }
    }

    private val logger: Logger = ssLogger<SSEngine>()

    lateinit var engineConfig: JSONConfig
        private set

    override fun create() {
        if (Gdx.app.type == Application.ApplicationType.Desktop) {
            Gdx.app.logLevel = Application.LOG_DEBUG
        }

        logger.info { "Starting Engine..." }
        setEmptyScreen()
    }

    private fun setEmptyScreen() {
        currentScreen.hide()
        currentScreen = emptyScreen()
        currentScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        currentScreen.show()
    }

    override fun <Type : Screen> setScreen(type: Class<Type>) {
        super.setScreen(type)
        logger.debug { "Set screen to $type" }
    }
}
