---
--- Created by glorantq.
--- DateTime: 2017. 10. 01. 08:37 PM
---
require "io.glorantq.simularscript.engine.api.LogApi"

local testVariable = 10

local function test2()
    Log.debug("Local function")
end

function render()
    Log.info("Hello from the second file")
    Log.info(testVariable)
    test2()
end