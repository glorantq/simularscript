---
--- Created by glorantq.
--- DateTime: 2017. 10. 16. 06:26 PM
---

require "io.glorantq.simularscript.engine.api.LogApi"
require "io.glorantq.simularscript.engine.api.multifile.MultifileApi"
require "io.glorantq.simularscript.engine.api.graphics.GraphicsApi"
require "io.glorantq.simularscript.engine.api.MathApi"
require "io.glorantq.simularscript.engine.api.GameApi"
require "io.glorantq.simularscript.engine.api.CameraApi"
require "io.glorantq.simularscript.engine.api.InputApi"

local image

function ss_main()
    Graphics.setClearColour(69, 0, 0) -- Red / Kék / Blue

    image = Graphics.makeSprite("jumpscare.jpg")
    image.setSize(Math.vec2(256, 256))
    image.setPosition(Math.vec2(0, 0))
end

function ss_render()
    if(Input.isKeyPressed("SPACE")) then
        image.setPosition(Input.getMousePosition().sub(Math.vec2(image.getWidth() / 2, image.getHeight() / 2)))
    end

    image.draw()

    if(Input.isKeyJustPressed("F11")) then
        Graphics.toggleFullscreen()
    end
end