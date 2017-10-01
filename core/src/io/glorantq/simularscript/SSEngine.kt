package io.glorantq.simularscript

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.Viewport
import io.glorantq.simularscript.config.JSONConfig
import io.glorantq.simularscript.engine.GameMeta
import io.glorantq.simularscript.engine.Window
import io.glorantq.simularscript.screens.GameScreen
import io.glorantq.simularscript.utils.*
import ktx.app.KtxGame
import ktx.app.emptyScreen
import ktx.log.Logger
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.luaj.vm2.ast.Str

class SSEngine private constructor() : KtxGame<Screen>(clearScreen = false, firstScreen = emptyScreen()) {
    private object Singleton {
        val INSTANCE: SSEngine = SSEngine()
    }

    companion object {
        val INSTANCE: SSEngine by lazy { Singleton.INSTANCE }
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
        if (Gdx.app.type == Application.ApplicationType.Desktop) {
            Gdx.app.logLevel = Application.LOG_DEBUG
        }

        logger.info { "Starting Engine..." }

        camera = OrthographicCamera()
        spriteBatch = SpriteBatch()

        engineConfig = JSONConfig("config.json")

        if(!engineConfig.hasKeys("window", "game", "meta")) {
            throw EngineInitialisationException("Invalid game config!")
        }

        val windowConfig: JSONObject = engineConfig.g("window")
        if(!windowConfig.hasKeys("width", "height", "title", "viewport", "fullscreen")) {
            throw EngineInitialisationException("Invalid window configuration!")
        }

        window = Window(windowConfig.g("width"), windowConfig.g("height"), windowConfig.g("fullscreen"), windowConfig.g("title"))

        Gdx.graphics.setTitle(window.title)

        if(window.fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
        } else {
            Gdx.graphics.setWindowedMode(window.width, window.height)
        }

        viewport = when(windowConfig["viewport"].toString()) {
            "fit" -> FitViewport(window.width.toFloat(), window.height.toFloat(), camera)
            "stretch" -> StretchViewport(window.width.toFloat(), window.height.toFloat(), camera)
            else -> throw EngineInitialisationException("Unsupported viewport!")
        }

        logger.info { "Window Configuration: $window" }

        val gameConfig: JSONObject = engineConfig.g("game")
        if(!gameConfig.hasKeys("baseColour", "useVsync", "scripting")) {
            throw EngineInitialisationException("Invalid game configuration!")
        }

        Gdx.graphics.setVSync(gameConfig.g("useVsync"))
        baseColour = (gameConfig["baseColour"] as Number).toInt().hexToOGL()

        val metaConfig: JSONObject = engineConfig.g("meta")
        gameMeta = GameMeta(metaConfig.g("name"), metaConfig.g("packageName"), metaConfig.g("author"))

        logger.info { "Game Meta: $gameMeta" }

        val scriptingConfig: JSONObject = gameConfig.g("scripting")
        if(!scriptingConfig.hasKeys("sourceDirectory", "sourceFiles", "mainFile")) {
            throw EngineInitialisationException("Invalid scripting config")
        }

        val sourceFiles: ArrayList<String> = arrayListOf()
        (scriptingConfig["sourceFiles"] as JSONArray).mapTo(sourceFiles) { it.toString() }

        gameScreen = GameScreen(scriptingConfig.g("sourceDirectory"), scriptingConfig.g("mainFile"), sourceFiles)
        setScreen(gameScreen)
        gameScreen.start()
    }

    override fun render() {
        ktx.app.clearScreen(baseColour[0], baseColour[1], baseColour[2])

        camera.position.x = (window.width / 2).toFloat()
        camera.position.y = (window.height / 2).toFloat()
        camera.update()

        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()

        super.render()

        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        viewport.update(width, height)
    }

    fun setScreen(screen: Screen) {
        currentScreen.hide()
        currentScreen = screen
        currentScreen.resize(Gdx.graphics.width, Gdx.graphics.height)
        currentScreen.show()
    }

    override fun <Type : Screen> setScreen(type: Class<Type>) {
        super.setScreen(type)
        logger.debug { "Set screen to $type" }
    }
}
