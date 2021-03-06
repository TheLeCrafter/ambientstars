package dev.thelecrafter.plugins.ambientstars

import dev.thelecrafter.plugins.ambientstars.commands.ReloadCommand
import dev.thelecrafter.plugins.ambientstars.utils.UpdateChecker
import io.papermc.lib.PaperLib
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

class AmbientStarsPlugin : JavaPlugin() {
    companion object {
        lateinit var getInstance: Plugin
        lateinit var getLogger: Logger
        lateinit var getDefaultConfig: FileConfiguration
        var task: BukkitTask? = null

        fun disableOnLag() {
            task = object : BukkitRunnable() {
                override fun run() {
                    if (getInstance.server.tps.last() <= 16) {
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
    }

    override fun onEnable() {
        getInstance = this
        getLogger = logger
        UpdateChecker.checkForUpdates()
        PaperLib.suggestPaper(getInstance, Level.WARNING)
        getInstance.saveDefaultConfig()
        getDefaultConfig = YamlConfiguration.loadConfiguration(getTextResource("config.yml")!!)
        if (!PaperLib.isPaper()) {
            logger.log(Level.WARNING, "This plugin will not work if you don't use Paper! Disabling...")
            Bukkit.getPluginManager().disablePlugin(getInstance)
        } else {
            Bukkit.getPluginManager().registerEvents(UpdateChecker(), getInstance)
            ShootingStars.init()
            registerCommand("reloadstars", ReloadCommand(), ReloadCommand())
            if (!getInstance.config.contains("config-version") || !getInstance.config.isInt("config-version") || getInstance.config.getInt("config-version") != getDefaultConfig.getInt("config-version")) {
                logger.log(Level.WARNING, "Invalid config version! Regenerating...")
                logger.log(Level.WARNING, "You can find your old configuration in the outdated_config.yml in the plugins data directory.")
                val values: MutableMap<String, Any> = mutableMapOf()
                for (key in config.getKeys(false)) {
                    values[key] = config.get(key)!!
                }
                val configFile = File(getInstance.dataFolder, "config.yml")
                configFile.copyTo(File(getInstance.dataFolder, "outdated_config.yml"), true)
                configFile.delete()
                getInstance.saveDefaultConfig()
                getInstance.reloadConfig()
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

    private fun registerCommand(name: String, executor: CommandExecutor, tabCompleter: TabCompleter) {
        getCommand(name)!!.setExecutor(executor)
        getCommand(name)!!.tabCompleter = tabCompleter
    }

    override fun onDisable() {
        logger.log(Level.ALL, "Shutting down internal galaxy")
    }
}