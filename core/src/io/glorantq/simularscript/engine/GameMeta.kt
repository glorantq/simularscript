package io.glorantq.simularscript.engine

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Data class storing information about the game
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param name Name of the game
 * @param packageName Package name of the game
 * @param author Author of the game
 */

class GameMeta(val name: String, val packageName: String, val author: String) {
    override fun toString(): String = ToStringBuilder.reflectionToString(this)
}