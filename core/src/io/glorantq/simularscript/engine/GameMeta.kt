package io.glorantq.simularscript.engine

import org.apache.commons.lang3.builder.ToStringBuilder

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */
class GameMeta(val name: String, val packageName: String, val author: String) {
    override fun toString(): String = ToStringBuilder.reflectionToString(this)
}