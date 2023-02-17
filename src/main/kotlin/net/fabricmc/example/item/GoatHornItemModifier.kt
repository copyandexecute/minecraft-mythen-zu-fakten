package net.fabricmc.example.item

import kotlinx.coroutines.cancel
import net.fabricmc.example.events.GoatHornCallback
import net.minecraft.entity.passive.GoatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Instrument
import net.minecraft.util.math.Box
import net.minecraft.world.World
import net.silkmc.silk.core.task.infiniteMcCoroutineTask

object GoatHornItemModifier : GoatHornCallback {
    fun init() {
        GoatHornCallback.EVENT.register(this)
    }
    override fun onGoating(world: World, player: PlayerEntity, instrument: Instrument) {
        val radius = 250.0
        val speed = 1.5
        world.getEntitiesByClass(GoatEntity::class.java, Box.of(player.pos, radius, radius, radius)) { true }
            .forEach { goat ->
                infiniteMcCoroutineTask {
                    if (goat.pos.distanceTo(player.pos) > 4) {
                        goat.navigation.startMovingTo(player, speed)
                    } else {
                        this.cancel()
                    }
                }
            }
    }
}