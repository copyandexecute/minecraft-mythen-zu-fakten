package net.fabricmc.example.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.mob.VexEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask
import kotlin.math.cos
import kotlin.math.sin

object VexEntityModifier {
    fun VexEntity.transform(beetroot: ItemStack) {
        this.isAiDisabled = true
        mcCoroutineTask(delay = 5.ticks) {
            val allayEntity = EntityType.ALLAY.create(this@transform.world)
            if (allayEntity != null) {
                this@transform.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, 0.2f, 1f)
                allayEntity.updatePosition(this@transform.x, this@transform.y, this@transform.z)
                allayEntity.yaw = this@transform.yaw
                allayEntity.pitch = this@transform.pitch
                beetroot.decrement(1)
                spawnCircle(this@transform.pos, world) {
                    (allayEntity as InvertedAllay).setInverted(true)
                    world.spawnEntity(allayEntity)
                    allayEntity.playSound(SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, 1f, 1f)
                    this@transform.discard()
                }
            }
        }
    }

    private fun spawnCircle(position: Vec3d, world: World, lastIterationCallback: Runnable? = null) {
        var heightAddition = 0.0
        repeat(10) { circle ->
            mcCoroutineTask(delay = circle.ticks) {
                if (circle + 1 == 10) {
                    lastIterationCallback?.run()
                }

                val radius = 1
                var t = 0.0
                while (t <= 2 * Math.PI * radius) {
                    val x = radius * cos(t) + position.x;
                    val z = position.z + radius * sin(t)

                    world.addParticle(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        x,
                        position.y + heightAddition,
                        z,
                        0.0,
                        0.0,
                        0.0
                    )

                    t += 0.05
                }
                heightAddition += 0.2
            }
        }
    }
}
