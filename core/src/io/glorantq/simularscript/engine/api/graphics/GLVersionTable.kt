package io.glorantq.simularscript.engine.api.graphics

import com.badlogic.gdx.graphics.glutils.GLVersion
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * LuaTable containing the OpenGL version used by the enigne
 *
 * @author Gerber Lóránt Viktor
 * @since 3.0-beta1
 *
 * @param version Version object from LibGDX
 */
class GLVersionTable(private val version: GLVersion) : LuaTable() {
    init {
        set("majorVersion", version.majorVersion)
        set("minorVersion", version.minorVersion)
        set("releaseVersion", version.releaseVersion)
        set("type", version.type.name)
        set("vendor", version.vendorString)
    }

    override fun tostring(): LuaValue = LuaValue.valueOf("GLVersion(${version.vendorString} ${version.type.name} ${version.majorVersion}.${version.minorVersion}.${version.releaseVersion})")
    override fun tojstring(): String = tostring().tojstring()
}