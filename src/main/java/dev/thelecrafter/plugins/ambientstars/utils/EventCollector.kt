package dev.thelecrafter.plugins.ambientstars.utils

import dev.thelecrafter.plugins.ambientstars.AmbientStarsPlugin
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import org.bukkit.Bukkit
import org.bukkit.event.Listener

object EventCollector {

    fun addAllEvents() {
        try {
            val result: ScanResult = ClassGraph().enableClassInfo().acceptPackages("dev.thelecrafter.kotlin.plugins.rpg.rpgmanager").scan()
            val classes: MutableList<Class<out Listener>> = (result.getClassesImplementing(Listener::class.java).loadClasses() as MutableList<Class<out Listener>>?)!!
            for (clazz in classes) {
                Bukkit.getPluginManager().registerEvents(clazz.getDeclaredConstructor().newInstance(), AmbientStarsPlugin.getInstance)
            }
        } catch (error: Exception) {
            AmbientStarsPlugin.getLogger.warning("There was an Error scanning the packages!")
        }
    }

}