package io.glorantq.simularscript

import com.badlogic.gdx.*
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.glorantq.simularscript.config.JSONConfig
import io.glorantq.simularscript.engine.GameMeta
import io.glorantq.simularscript.engine.Window
import io.glorantq.simularscript.input.GestureHandler
import io.glorantq.simularscript.screens.CrashScreen
import io.glorantq.simularscript.screens.GameScreen
import io.glorantq.simularscript.utils.*
import ktx.app.KtxGame
import ktx.app.emptyScreen
import ktx.log.Logger
import org.json.simple.JSONArray
import org.json.simple.JSONObject

/**
 * Main engine class
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class SSEngine private constructor() : KtxGame<Screen>(clearScreen = false, firstScreen = emptyScreen()) {
    private object Singleton {
        val INSTANCE: SSEngine = SSEngine()
    }

    companion object {
        val INSTANCE: SSEngine by lazy { Singleton.INSTANCE }
        val VERSION: String = "3.0-beta1"

        val WIDTH: Float
            get() = INSTANCE.window.width.toFloat()

        val HEIGHT: Float
            get() = INSTANCE.window.height.toFloat()
    }

    private val logger: Logger = ssLogger<SSEngine>()

    lateinit var engineConfig: JSONConfig
        private set
    lateinit var gameMeta: GameMeta
        private set

    lateinit var camera: OrthographicCamera
        private set
    lateinit var viewport: Viewport
        private set
    lateinit var spriteBatch: SpriteBatch
        private set

    lateinit var window: Window
        private set

    private lateinit var baseColour: FloatArray

    lateinit var gameScreen: GameScreen
        private set

    override fun create() {
        try {
            if (Gdx.app.type == Application.ApplicationType.Desktop) {
                Gdx.app.logLevel = Application.LOG_DEBUG
            }

            logger.info { "Starting Engine..." }

            camera = OrthographicCamera()
            spriteBatch = SpriteBatch()

            val multiplexer = InputMultiplexer(GestureDetector(GestureHandler.INSTANCE), InputAdapter())
            Gdx.input.inputProcessor = multiplexer

            engineConfig = JSONConfig("config.json")

            if (!engineConfig.hasKeys("window", "game", "meta")) {
                throw EngineInitialisationException("Invalid game config!")
            }

            val windowConfig: JSONObject = engineConfig.g("window")
            if (!windowConfig.hasKeys("width", "height", "title", "viewport", "fullscreen")) {
                throw EngineInitialisationException("Invalid window configuration!")
            }

            window = Window(windowConfig.g("width"), windowConfig.g("height"), windowConfig.g("fullscreen"), windowConfig.g("title"))

            Gdx.graphics.setTitle(window.title)

            if (window.fullscreen) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            } else {
                Gdx.graphics.setWindowedMode(window.width, window.height)
            }

            viewport = when (windowConfig["viewport"].toString()) {
                "fit" -> FitViewport(window.width.toFloat(), window.height.toFloat(), camera)
                "stretch" -> StretchViewport(window.width.toFloat(), window.height.toFloat(), camera)
                else -> throw EngineInitialisationException("Unsupported viewport!")
            }

            logger.info { "Window Configuration: $window" }

            val gameConfig: JSONObject = engineConfig.g("game")
            if (!gameConfig.hasKeys("useVsync", "scripting")) {
                throw EngineInitialisationException("Invalid game configuration!")
            }

            Gdx.graphics.setVSync(gameConfig.g("useVsync"))
            baseColour = floatArrayOf(0f, 0f, 0f)

            val metaConfig: JSONObject = engineConfig.g("meta")
            gameMeta = GameMeta(metaConfig.g("name"), metaConfig.g("packageName"), metaConfig.g("author"))

            logger.info { "Game Meta: $gameMeta" }

            val scriptingConfig: JSONObject = gameConfig.g("scripting")
            if (!scriptingConfig.hasKeys("sourceDirectory", "sourceFiles", "mainFile")) {
                throw EngineInitialisationException("Invalid scripting config")
            }

            val sourceFiles: ArrayList<String> = arrayListOf()
            (scriptingConfig["sourceFiles"] as JSONArray).mapTo(sourceFiles) { it.toString() }

            camera.position.set((window.width / 2).toFloat(), (window.height / 2).toFloat(), 0f)

            gameScreen = GameScreen(scriptingConfig.g("sourceDirectory"), scriptingConfig.g("mainFile"), sourceFiles)
            gameScreen.start()
            setScreen(gameScreen)
        } catch (e: Exception) {
            crash(e)

            Gdx.input.inputProcessor = InputMultiplexer()
        }
    }

    override fun render() {
        if(currentScreen is CrashScreen) {
            currentScreen.render(Gdx.graphics.deltaTime)
            return
        }

        try {
            ktx.app.clearScreen(baseColour[0], baseColour[1], baseColour[2])

            camera.update()

            spriteBatch.projectionMatrix = camera.combined
            spriteBatch.begin()

            super.render()

            spriteBatch.end()
        } catch (e: Exception) {
            crash(e)
        }
    }

    override fun resize(width: Int, height: Int) {
        if(currentScreen is CrashScreen) {
            currentScreen.resize(width, height)
            return
        }

        super.resize(width, height)
        viewport.update(width, height)
    }

    private fun setScreen(screen: Screen) {
        currentScreen.hide()
        currentScreen = screen
        currentScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        currentScreen.show()
    }

    fun crash(e: Exception) {
        setScreen(CrashScreen(e))
        e.printStackTrace()
    }

    override fun <Type : Screen> setScreen(type: Class<Type>) {
        super.setScreen(type)
        logger.debug { "Set screen to $type" }
    }
}
