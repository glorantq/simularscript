require "io.glorantq.simularscript.engine.api.LogApi"
require "io.glorantq.simularscript.engine.api.multifile.MultifileApi"
require "io.glorantq.simularscript.engine.api.graphics.GraphicsApi"
require "io.glorantq.simularscript.engine.api.MathApi"
require "io.glorantq.simularscript.engine.api.GameApi"
require "io.glorantq.simularscript.engine.api.CameraApi"

initialised = false
local winSprite

function main()
    winSprite = Graphics.makeSprite("c2e.jpg")
    winSprite.setSize(Math.vec2(Graphics.getWidth(), Graphics.getHeight()))

    initialised = true
end

function render()
    winSprite.draw()
end