package net.fabricmc.example.myth.myths

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import net.fabricmc.example.entity.RocketSpammer
import net.fabricmc.example.myth.Myth
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3d
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.broadcastText
import net.silkmc.silk.core.text.literal
import java.util.UUID

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
        //world.server?.broadcastText(("E: $e, Rockets: " + spammer.getUsedRockets()).literal)
        velocity = vec3d2.add(
            vec3d.x * e + (vec3d.x * d - vec3d2.x) * 0.5,
            vec3d.y * e + (vec3d.y * d - vec3d2.y) * 0.5,
            vec3d.z * e + (vec3d.z * d - vec3d2.z) * 0.5
        )
    }
}