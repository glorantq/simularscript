package io.glorantq.simularscript.config

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import io.glorantq.simularscript.utils.ConfigException
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

/**
 * Utility class to parse and read JSON configurations
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param handle The FileHandle to the config file
 */

class JSONConfig(handle: FileHandle) {
    constructor(name: String): this(Gdx.files.internal(name))

    /**
     * Map containing keys mapped to generic Objects
     */
    private val contents: MutableMap<String, Any> = mutableMapOf()

    init {
        if(!handle.exists()) {
            throw ConfigException("Tried to load non-existent JSON file!")
        }

        try {
            val root: JSONObject = JSONParser().parse(handle.readString()) as JSONObject

            for((key, value) in root) {
                if(value == null) {
                    throw ConfigException("Configs cannot contain null values")
                }

                contents.put(key.toString(), value)
            }
        } catch (e: Exception) {
            throw ConfigException("Failed to parse JSON!", e)
        }
    }

    operator fun get(key: String): Any? = contents[key]
    fun <E> g(key: String): E = contents[key] as E
    fun contains(key: String): Boolean = contents.containsKey(key)
    fun hasKeys(vararg keys: String): Boolean = !keys.any { !contains(it) }
}