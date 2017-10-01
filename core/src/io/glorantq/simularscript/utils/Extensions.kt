package io.glorantq.simularscript.utils

import org.apache.commons.lang3.StringUtils

/**
 * Created by Gerber Lóránt on 2017. 10. 01..
 */

fun String.split(n: Int): List<String> = split(Regex("(?<=\\G.{$n})"))

fun Int.rgbToOGL(): Float = (1f / 255f) * toFloat()

fun Int.hexToRGB(): IntArray {
    val hexString: String = StringUtils.leftPad(toString(16), 6, '0')
    val parts: List<String> = hexString.split(2)

    if(parts.size < 3) {
        throw EngineException("Failed to parse Hex colour: $this")
    }

    return intArrayOf(parts[0].toInt(16), parts[1].toInt(16), parts[2].toInt(16))
}

fun Int.hexToOGL(): FloatArray {
    val rgb: IntArray = hexToRGB()

    return floatArrayOf(rgb[0].rgbToOGL(), rgb[1].rgbToOGL(), rgb[2].rgbToOGL())
}

fun Number.roundToNearest(x: Int): Number = Math.round(toFloat() / x.toFloat()) * x.toFloat()
fun Number.floorToNearest(x: Int): Number = Math.floor(toDouble() / x.toFloat()) * x.toFloat()
fun Number.ceilToNearest(x: Int): Number = Math.ceil(toDouble() / x.toFloat()) * x.toFloat()