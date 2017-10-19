require "io.glorantq.simularscript.engine.api.LogApi"
require "io.glorantq.simularscript.engine.api.multifile.MultifileApi"
require "io.glorantq.simularscript.engine.api.graphics.GraphicsApi"
require "io.glorantq.simularscript.engine.api.MathApi"
require "io.glorantq.simularscript.engine.api.GameApi"
require "io.glorantq.simularscript.engine.api.CameraApi"
require "io.glorantq.simularscript.engine.api.io.FileApi"

local sprites = {}
local winScreen

local speed = 500

function ss_main()
    sprites[-1] = 10

    for i = 1, sprites[-1] do
        local sprite = Graphics.makeSprite(FileIO.getInternal(Math.choose("badlogic.jpg", "jumpscare.jpg")))
        sprite.setSize(256, 256)
        sprite.setPosition(Math.random(Graphics.getWidth() - 64))
        sprite.clickHandler = function()
            sprite.dispose()
            sprites[i] = nil
        end
        sprite.xDirection = Math.choose(1, -1)
        sprite.yDirection = Math.choose(1, -1)
        sprites[i] = sprite
    end

    winScreen = __ "winScreen"

    Log.info(Math.avg(1))
    Log.info(Math.avg(1, 0))
    Log.info(Math.avg(1, 2, 3))
    Log.info(Math.avg(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    Log.info(Math.avg(1423, 423, 76555, 123))

    Log.info(Math.sum(1, 2, 3, 4))
end

function ss_render()
    if (check_win()) then
        if not winScreen.initialised then
            winScreen.main()
        end

        winScreen.render()
        return
    end

    for i = 1, sprites[-1] do
        if sprites[i] ~= nil then
            local sprite = sprites[i]
            sprite.draw()

            local position = sprite.getPosition()
            local change = Math.vec2(speed * Graphics.getDeltaTime() * sprite.xDirection, speed * Graphics.getDeltaTime() * sprite.yDirection)
            position = position.add(change)
            sprite.setPosition(position)

            if position.x > Graphics.getWidth() - sprite.getWidth() then
                sprite.xDirection = -1
            end

            if position.x < 0 then
                sprite.xDirection = 1
            end

            if position.y > Graphics.getHeight() - sprite.getHeight() then
                sprite.yDirection =  -1
            end

            if position.y < 0 then
                sprite.yDirection = 1
            end
        end
    end
end

function check_win()
    for i = 1, sprites[-1] do
        if (sprites[i] ~= nil and sprites[i].isVisible()) then
            return false
        end
    end

    return true
end