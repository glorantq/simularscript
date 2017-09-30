package io.glorantq.simularscript.utils

/**
 * Created by Gerber Lóránt on 2017. 09. 30..
 */

class EngineInitialisationException : Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Exception): super(message, cause)
    constructor(cause: Exception): super(cause)
    constructor(): super()
}

class ConfigException : Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Exception): super(message, cause)
    constructor(cause: Exception): super(cause)
    constructor(): super()
}