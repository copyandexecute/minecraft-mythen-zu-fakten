package net.fabricmc.example.myth.myths

import kotlinx.coroutines.Job
import net.fabricmc.example.extensions.toId
import net.fabricmc.example.mixin.accessor.InGameHudAccessor
import net.fabricmc.example.mixin.gui.hud.InGameHudMixin
import net.fabricmc.example.myth.Myth
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.hit.HitResult
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literal
import kotlin.math.abs

object BlindingMyth : Myth("Blinding", true) {
    private val START = Pair(0, -10f)
    private val ZENIT = Pair(6000, -90f)
    private val STEIGUNG = (ZENIT.second - START.second) / (ZENIT.first - START.first)
    private var job: Job? = null
    private val BRIGHTNESS = "textures/misc/blind.png".toId()

    override fun appendCommand(commandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        commandBuilder.literal("start") {
            runs {
                val server = this.source.server
                job?.cancel()
                job = infiniteMcCoroutineTask {
                    server.playerManager.playerList.forEach {
                        it.sendMessage(it.isLookingAtSun.toString().literal)
                    }
                }
            }
        }
    }

    private val PlayerEntity.isLookingAtSun: Boolean
        get() {
            val sunPitch = timeToPitch(world.lunarTime)
            if (raycast(100.0, 0.0F, true).type != HitResult.Type.MISS) return false
            if (pitch <= sunPitch && pitch >= sunPitch - 9f) {
                if (abs(yaw) in 80F..100F) {
                    return true
                }
            }
            return false
        }

    private fun timeToPitch(time: Long): Float {
        val zeit = if (time > 6000L) 12000 - time else time
        return START.second + STEIGUNG * (zeit - START.first)
    }

    fun renderOverlay(
        inGameHud: InGameHud,
        matrices: MatrixStack
    ) {
        (inGameHud as InGameHudAccessor).invokeRenderOverlay(matrices, BRIGHTNESS, 0.95f)
    }
}

interface SunnyPlayer {
    fun hasStartedLookingAtSun(): Boolean
    fun setHasStartedLookingAtSun(flag: Boolean)
    fun setSunTicks(amount: Int)
    fun getSunTicks(): Int
}