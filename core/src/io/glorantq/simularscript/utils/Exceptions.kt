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

class EngineException : Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Exception): super(message, cause)
    constructor(cause: Exception): super(cause)
    constructor(): super()
}

class ScriptingException : Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Exception): super(message, cause)
    constructor(cause: Exception): super(cause)
    constructor(): super()
}

class GameException : Exception {
    constructor(message: String): super(message)
    constructor(message: String, cause: Exception): super(message, cause)
    constructor(cause: Exception): super(cause)
    constructor(): super()
}