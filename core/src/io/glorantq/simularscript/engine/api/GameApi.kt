package io.glorantq.simularscript.engine.api

import com.badlogic.gdx.Gdx
import io.glorantq.simularscript.utils.GameException
import io.glorantq.simularscript.utils.ssLogger
import ktx.log.Logger
import ktx.log.logger
import org.apache.commons.lang3.exception.ExceptionUtils
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.LibFunction
import org.luaj.vm2.lib.TwoArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

/**
 * API for interacting with engine-related methods
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 */

class GameApi : TwoArgFunction() {
    companion object {
        private val logger: Logger = ssLogger<GameApi>()
    }

    override fun call(modname: LuaValue, env: LuaValue): LuaValue {
        val library: LuaTable = LuaValue.tableOf()
        library.set("close", Close())
        library.set("throw", Throw())
        library.set("try", Try())
        env.set("Game", library)
        env.set("App", library)
        return library
    }

    private class Close : ZeroArgFunction() {
        override fun call(): LuaValue {
            Gdx.app.exit()
            return LuaValue.NONE
        }
    }

    private class Throw : LibFunction() {
        override fun call(): LuaValue {
            throw GameException()
        }

        override fun call(a: LuaValue): LuaValue {
            throw GameException(a.tojstring())
        }

        override fun call(a: LuaValue, b: LuaValue): LuaValue {
            throw GameException(a.tojstring(), GameException(b.tojstring()))
        }
    }

    private class Try : LibFunction() {
        override fun call(a: LuaValue): LuaValue {
            val tryFunction: LuaFunction = a.checkfunction()

            try {
                tryFunction.call()
            } catch (e: Exception) {
                logger.error { "Caught exception ${e::class.simpleName} while executing ${tryFunction.name()}!" }
            }

            return LuaValue.NONE
        }

        override fun call(a: LuaValue, b: LuaValue): LuaValue {
            val tryFunction: LuaFunction = a.checkfunction()
            val catchFunction: LuaFunction = b.checkfunction()

            try {
                tryFunction.call()
            } catch (e: Exception) {
                catchFunction.call(LuaValue.valueOf(e::class.simpleName), LuaValue.valueOf(ExceptionUtils.getMessage(e)), LuaValue.valueOf(ExceptionUtils.getStackTrace(e)))
                logger.error { "Caught exception ${e::class.simpleName} while executing ${tryFunction.name()}!" }
            }

            return LuaValue.NONE
        }
    }
}