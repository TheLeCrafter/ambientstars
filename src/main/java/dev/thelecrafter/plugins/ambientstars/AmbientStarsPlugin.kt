package dev.thelecrafter.plugins.ambientstars

import dev.thelecrafter.plugins.ambientstars.utils.EventCollector
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Level
import java.util.logging.Logger

class AmbientStarsPlugin : JavaPlugin() {
    companion object {
        lateinit var getInstance: Plugin
        lateinit var getLogger: Logger
        lateinit var getDefaultConfig: FileConfiguration
    }

    override fun onEnable() {
        getInstance = this
        getLogger = logger
        PaperLib.suggestPaper(getInstance, Level.WARNING)
        getInstance.saveDefaultConfig()
        getDefaultConfig = YamlConfiguration.loadConfiguration(getTextResource("config.yml")!!)
        if (!PaperLib.isPaper()) {
            logger.log(Level.WARNING, "[AmbientStars] This plugin will not work if you don't use Paper! Disabling...")
            Bukkit.getPluginManager().disablePlugin(getInstance)
        } else {
            EventCollector.addAllEvents()
            ShootingStars.init()
            if (!getInstance.config.contains("config-version") || !getInstance.config.isInt("config-version") || getInstance.config.getInt("config-version") != getDefaultConfig.getInt("config-version")) {
                logger.log(Level.WARNING, "[AmbientStars] Invalid config version! Regenerating...")
                getInstance.config.load(getTextResource("config.yml")!!)
                getInstance.saveConfig()
            }
            for (key in getDefaultConfig.getKeys(false)) {
                if (!getInstance.config.contains(key)) {
                    getInstance.config.set(key, getDefaultConfig.get(key))
                    getInstance.saveConfig()
                }
            }
            if (getInstance.config.getBoolean("auto-disable-on-lag", getDefaultConfig.getBoolean("auto-disable-on-lag"))) {
                disableOnLag()
            }
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