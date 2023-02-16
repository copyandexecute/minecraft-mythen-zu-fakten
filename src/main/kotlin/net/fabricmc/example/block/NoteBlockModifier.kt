package net.fabricmc.example.block

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask

object NoteBlockModifier {
    fun explode(world: World, pos: BlockPos) {
        mcCoroutineTask(delay = (5 * 20).ticks) {
            runCatching {
                world.createExplosion(
                    null,
                    pos.x.toDouble(),
                    pos.y.toDouble(),
                    pos.z.toDouble(),
                    8f,
                    World.ExplosionSourceType.BLOCK
                )
            }
        }
    }
}
