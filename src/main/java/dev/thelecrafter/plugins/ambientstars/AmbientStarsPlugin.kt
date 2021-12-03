package dev.thelecrafter.plugins.ambientstars

import dev.thelecrafter.plugins.ambientstars.utils.EventCollector
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
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
        PaperLib.suggestPaper(getInstance, Level.WARNING)
        if (!PaperLib.isPaper()) {
            logger.log(Level.WARNING, "[AmbientStars] This plugin will not work if you don't use Paper! Disabling...")
            Bukkit.getPluginManager().disablePlugin(getInstance)
        } else {
            EventCollector.addAllEvents()
            ShootingStars.init()
            disableOnLag()
        }
    }

    private fun disableOnLag() {
        object : BukkitRunnable() {
            override fun run() {
                if (server.tps.last() <= 16) {
                    if (!ShootingStars.task.isCancelled) {
                        ShootingStars.task.cancel()
                    }
                } else {
                    if (ShootingStars.task.isCancelled) {
                        ShootingStars.init()
                    }
                }
            }
        }.runTaskTimer(getInstance, 0, 60 * 20)
    }

    override fun onDisable() {
        logger.log(Level.FINE, "[AmbientStars] Shutting down internal galaxy")
    }
}