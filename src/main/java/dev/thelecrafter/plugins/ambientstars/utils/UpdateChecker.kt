package dev.thelecrafter.plugins.ambientstars.utils

import dev.thelecrafter.plugins.ambientstars.AmbientStarsPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.kohsuke.github.GHRelease
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHub
import java.util.logging.Level

class UpdateChecker : Listener {

    companion object {
        private var upToDate: Boolean = true

        fun checkForUpdates() {
            val version: String = "v" + AmbientStarsPlugin.getInstance.description.version
            val gitHub: GitHub = GitHub.connectAnonymously()
            val repository: GHRepository = gitHub.getRepository("TheLeCrafter/ambientstars")!!
            val latestRelease: GHRelease = repository.latestRelease
            if (!latestRelease.isDraft && !latestRelease.isPrerelease) {
                if (version != latestRelease.tagName) {
                    upToDate = false
                    AmbientStarsPlugin.getLogger.log(Level.WARNING, "The plugin is not up to date! Download the latest release at https://github.com/TheLeCrafter/ambientstars/releases")
                }
            } else AmbientStarsPlugin.getLogger.log(Level.WARNING, "Couldn't check the latest release because it's a draft/pre release! Check yourself at https://github.com/TheLeCrafter/ambientstars/releases")
        }

        fun getUpToDate(): Boolean {
            return upToDate
        }
    }

    @EventHandler
    fun sendUpdateMessageOnOPJoin(event: PlayerJoinEvent) {
        if (event.player.isOp || event.player.hasPermission("*")) {
            if (!getUpToDate()) {
                event.player.sendMessage(Component.text("[AmbientStars] The plugin is not up to date! Download the latest release ").color(NamedTextColor.YELLOW).append(Component.text("here").decorate(TextDecoration.UNDERLINED).color(NamedTextColor.BLUE).clickEvent(
                    ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/TheLeCrafter/ambientstars/releases")).hoverEvent(HoverEvent.showText(
                    Component.text("Opens https://github.com/TheLeCrafter/ambientstars/releases").color(NamedTextColor.GRAY)))))
            }
        }
    }

}