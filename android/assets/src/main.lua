require "io.glorantq.simularscript.engine.api.LogApi"
require "io.glorantq.simularscript.engine.api.multifile.MultifileApi"

function main()
    Log.info("Szilya mádol jó a simularscript")
    Log.error("nem teccik a látvány!!")
    Log.debug("Ugyee")

    screenFile = Multifile.getFile("screenFile")
    screenFile.render()
end
