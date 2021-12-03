package dev.thelecrafter.plugins.ambientstars

import dev.thelecrafter.plugins.ambientstars.utils.EventCollector
import io.papermc.lib.PaperLib
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Level
import java.util.logging.Logger

class AmbientStarsPlugin : JavaPlugin() {
    companion object {
        lateinit var getInstance: Plugin
        lateinit var getLogger: Logger
    }

    override fun onEnable() {
        getInstance = this
        getLogger = logger
        PaperLib.suggestPaper(getInstance, Level.ALL)
        EventCollector.addAllEvents()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}