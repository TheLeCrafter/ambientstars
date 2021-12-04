package dev.thelecrafter.plugins.ambientstars

import org.bukkit.*
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import javax.annotation.Nonnull
import kotlin.random.Random

object ShootingStars {

    lateinit var task: BukkitTask

    fun init() {
        task = object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.location.world.environment == World.Environment.NORMAL || !AmbientStarsPlugin.getInstance.config.getBoolean("only-overworld", AmbientStarsPlugin.getDefaultConfig.getBoolean("only-overworld"))) {
                        if (player.location.world.time in 13000..23000 || !AmbientStarsPlugin.getInstance.config.getBoolean("only-night", AmbientStarsPlugin.getDefaultConfig.getBoolean("only-night"))) {
                            val location: Location = player.location.clone()
                            location.set(location.x + Random.nextDouble(26.0) - 13.0, location.y + Random.nextDouble(25.0, 35.0), location.z + Random.nextDouble(26.0) - 13.0)
                            if (location.block.type == Material.AIR) {
                                shootStar(location)
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(AmbientStarsPlugin.getInstance, 0, 20 * AmbientStarsPlugin.getInstance.config.getLong("time-per-shooting-star", AmbientStarsPlugin.getDefaultConfig.getLong("time-per-shooting-star")))
    }

    fun shootStar(@Nonnull location: Location) {
        val deltaX: Double = Random.nextDouble(8.0) - 4.0
        val deltaY: Double = Random.nextDouble(2.0)
        val deltaZ: Double = Random.nextDouble(8.0) - 4.0
        location.world.spawnParticle(Particle.FIREWORKS_SPARK, location, 0, deltaX, deltaY, deltaZ, Random.nextDouble(0.6), null, true)
    }

}