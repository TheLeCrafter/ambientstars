package dev.thelecrafter.plugins.ambientstars

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import javax.annotation.Nonnull
import kotlin.random.Random

object ShootingStars {

    lateinit var task: BukkitTask

    fun init() {
        var time: Long = AmbientStarsPlugin.getInstance.config.getLong("time-per-shooting-star", AmbientStarsPlugin.getDefaultConfig.getLong("time-per-shooting-star"))
        if (time <= 0) {
            time = AmbientStarsPlugin.getDefaultConfig.getLong("time-per-shooting-star")
        }
        task = object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    if (player.location.world.environment == World.Environment.NORMAL || !AmbientStarsPlugin.getInstance.config.getBoolean("only-overworld", AmbientStarsPlugin.getDefaultConfig.getBoolean("only-overworld"))) {
                        if (player.location.world.time in 13000..23000 || !AmbientStarsPlugin.getInstance.config.getBoolean("only-night", AmbientStarsPlugin.getDefaultConfig.getBoolean("only-night"))) {
                            val location: Location = player.location.clone()
                            location.add(Random.nextDouble(26.0) - 13.0, Random.nextDouble(25.0, 35.0), Random.nextDouble(26.0) - 13.0)
                            if (location.block.type == Material.AIR) {
                                shootStar(location, player)
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(AmbientStarsPlugin.getInstance, 0, 20 * time)
    }

    fun shootStar(@Nonnull location: Location, @Nonnull player: Player) {
        val deltaX: Double = Random.nextDouble(8.0) - 4.0
        val deltaY: Double = Random.nextDouble(2.0)
        val deltaZ: Double = Random.nextDouble(8.0) - 4.0
        if (AmbientStarsPlugin.getInstance.config.getBoolean("show-to-all-players", AmbientStarsPlugin.getDefaultConfig.getBoolean("show-to-all-players"))) {
            var radius: Int = AmbientStarsPlugin.getInstance.config.getInt("show-to-all-players-radius", AmbientStarsPlugin.getDefaultConfig.getInt("show-to-all-players-radius"))
            if (radius <= 0) {
                radius = AmbientStarsPlugin.getDefaultConfig.getInt("show-to-all-players-radius")
            }
            val builder: ParticleBuilder = getShootingStarParticleBuilderBase(location, deltaX, deltaY, deltaZ)
            builder.receivers(radius)
            builder.spawn()
        } else {
            val builder: ParticleBuilder = getShootingStarParticleBuilderBase(location, deltaX, deltaY, deltaZ)
            builder.receivers(player)
            builder.spawn()
        }
    }

    fun getShootingStarParticleBuilderBase(location: Location, deltaX: Double, deltaY: Double, deltaZ: Double): ParticleBuilder {
        val builder = ParticleBuilder(Particle.FIREWORKS_SPARK)
        builder.location(location)
        builder.count(0)
        builder.offset(deltaX, deltaY, deltaZ)
        builder.extra(Random.nextDouble(0.6))
        builder.force(true)
        return builder
    }

}