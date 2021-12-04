package dev.thelecrafter.plugins.ambientstars.commands

import dev.thelecrafter.plugins.ambientstars.AmbientStarsPlugin
import dev.thelecrafter.plugins.ambientstars.ShootingStars
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class ReloadCommand : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        AmbientStarsPlugin.getInstance.reloadConfig()
        sender.sendMessage(Component.text("[AmbientStars] Config reloaded!").color(NamedTextColor.GREEN))
        ShootingStars.task.cancel()
        ShootingStars.init()
        if (AmbientStarsPlugin.task != null && !AmbientStarsPlugin.task!!.isCancelled) {
            AmbientStarsPlugin.task!!.cancel()
        }
        if (AmbientStarsPlugin.getInstance.config.getBoolean("auto-disable-on-lag", AmbientStarsPlugin.getDefaultConfig.getBoolean("auto-disable-on-lag"))) {
            AmbientStarsPlugin.disableOnLag()
        }
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
       return mutableListOf()
    }
}