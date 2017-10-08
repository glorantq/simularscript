package io.glorantq.simularscript.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.StretchViewport
import ktx.app.KtxScreen
import ktx.app.clearScreen
import org.apache.commons.lang3.exception.ExceptionUtils

/**
 * A screen shown when an uncaught exception is thrown. Completely independent from main engine code.
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */
class CrashScreen(private val errorMessage: String = "", private val stackTrace: String = "") : KtxScreen {
    constructor(errorMessage: String = "", exception: Exception): this(errorMessage, ExceptionUtils.getStackTrace(exception))
    constructor(exception: Exception): this(ExceptionUtils.getMessage(exception), ExceptionUtils.getStackTrace(exception))

    private val font: BitmapFont = BitmapFont()
    private val layout: GlyphLayout = GlyphLayout()
    private val camera: OrthographicCamera = OrthographicCamera()
    private val viewport: StretchViewport = StretchViewport(1280f, 720f, camera)
    private val spriteBatch: SpriteBatch = SpriteBatch()

    override fun show() {
        Gdx.graphics.setTitle("Game Crashed :( - $errorMessage")
        Gdx.graphics.setWindowedMode(viewport.worldWidth.toInt(), viewport.worldHeight.toInt())

        font.color = Color.WHITE
    }

    override fun render(delta: Float) {
        clearScreen(0f, 0f, 0f, 1f)

        camera.position.x = viewport.worldWidth / 2f
        camera.position.y = viewport.worldHeight / 2f
        camera.update()

        spriteBatch.projectionMatrix = camera.combined
        spriteBatch.begin()

        drawCenteredText("Oops! Looks like your game has crashed.", Color.WHITE, viewport.worldWidth / 2f, viewport.worldHeight - 20f, 4.0f)
        drawWrappedCenter("Cause:\n$errorMessage\n\nStack Trace:\n$stackTrace", Color.WHITE, viewport.worldWidth / 2f, viewport.worldHeight - 100f, 1.65f, viewport.worldWidth - 200f)

        spriteBatch.end()
    }

    private fun drawWrappedCenter(text: String, color: Color, x: Float, y: Float, scale: Float, width: Float) {
        var xVar: Float = x
        font.data.setScale(scale)
        font.color = color
        xVar -= width / 2
        font.draw(spriteBatch, text, xVar, y, width, Align.center, true)
    }

    private fun drawCenteredText(text: String, color: Color, x: Float, y: Float, scale: Float) {
        var xVar: Float = x
        font.data.setScale(scale)
        layout.setText(font, text)
        xVar -= layout.width / 2
        font.color = color
        font.draw(spriteBatch, text, xVar, y)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}