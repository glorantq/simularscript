package io.glorantq.simularscript.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import io.glorantq.simularscript.SSEngine

fun main(arg: Array<String>) {
    val config = LwjglApplicationConfiguration()
    LwjglApplication(SSEngine.INSTANCE, config)
}

class DesktopLauncher {

}