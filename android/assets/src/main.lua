require "io.glorantq.simularscript.engine.api.LogApi"
require "io.glorantq.simularscript.engine.api.multifile.MultifileApi"
require "io.glorantq.simularscript.engine.api.graphics.GraphicsApi"
require "io.glorantq.simularscript.engine.api.MathApi"
require "io.glorantq.simularscript.engine.api.GameApi"
require "io.glorantq.simularscript.engine.api.CameraApi"

local testSprite
local testSpritePos

local rot = Camera.getBaseRotation()

function ss_main()
    testSprite = Gfx.makeSprite("badlogic.jpg")
    testSprite.clickHandler = onClick_testSprite
    testSpritePos = Math.vec2(Gfx.getWidth() / 2 - testSprite.getWidth() / 2, Gfx.getHeight() / 2 - testSprite.getHeight() / 2)
    testSprite.setPosition(testSpritePos)
end

function ss_render()
    testSprite.draw()

    Camera.rotate(rot)
    Log.info(Camera.getRotation())

    rot = Camera.getRotation() + 1
end