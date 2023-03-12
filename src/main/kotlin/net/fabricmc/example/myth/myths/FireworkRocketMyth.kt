package net.fabricmc.example.myth.myths

import kotlinx.coroutines.cancel
import net.fabricmc.example.entity.RocketSpammer
import net.fabricmc.example.myth.Myth
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MovementType
import net.minecraft.entity.projectile.FireworkRocketEntity
import net.minecraft.util.math.Vec3d
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import java.util.*

object FireworkRocketMyth : Myth("Firework") {
    private val checkers = mutableSetOf<UUID>()

    fun LivingEntity.setFireworkVelocity(vec: Vec3d) {
        if (!isActive or (this !is RocketSpammer)) {
            velocity = vec
            return
        }

        val spammer = this as RocketSpammer

        if (uuid !in checkers) {
            checkers.add(uuid)
            infiniteMcCoroutineTask {
                if (System.currentTimeMillis() > getLastUsedRocket() + 1000L) {
                    checkers.remove(uuid)
                    spammer.resetRockets()
                    this.cancel()
                }
            }
        }

        val vec3d: Vec3d = rotationVector
        val d = 1.5
        val e = 0.1 * Math.max(1, getUsedRockets())
        val vec3d2: Vec3d = velocity
        velocity = vec3d2.add(
            vec3d.x * e + (vec3d.x * d - vec3d2.x) * 0.5,
            vec3d.y * e + (vec3d.y * d - vec3d2.y) * 0.5,
            vec3d.z * e + (vec3d.z * d - vec3d2.z) * 0.5
        )
    }

    fun FireworkRocketEntity.apply(entity: Entity) {
        if (isActive && entity is LivingEntity) {
            (this as FireworkRocketEntityMyth).setShooter(entity)
        }
    }

    fun FireworkRocketEntity.positionInjection(x: Double, y: Double, z: Double, shooter: LivingEntity?) {
        if (!isActive or (shooter == null)) {
            setPosition(x, y, z)
        }
    }

    fun FireworkRocketEntity.velocityInjection(vec3d: Vec3d, shooter: LivingEntity?) {
        if (!isActive or (shooter == null)) {
            velocity = vec3d
        } else {
            val vec3d3 = velocity
            move(MovementType.SELF, vec3d3)
            velocity = vec3d3
        }
    }
}

interface FireworkRocketEntityMyth {
    fun setShooter(livingEntity: LivingEntity)
}
