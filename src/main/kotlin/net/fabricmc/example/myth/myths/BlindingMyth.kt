package net.fabricmc.example.myth.myths

import kotlinx.coroutines.Job
import net.fabricmc.example.extensions.toId
import net.fabricmc.example.myth.Myth
import net.minecraft.client.gl.JsonEffectShaderProgram
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.hit.HitResult
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literal
import org.jetbrains.annotations.ApiStatus.Internal
import kotlin.math.abs

object BlindingMyth : Myth("Blinding", true) {
    private val START = Pair(0, -10f)
    private val ZENIT = Pair(6000, -90f)
    private val STEIGUNG = (ZENIT.second - START.second) / (ZENIT.first - START.first)
    private var job: Job? = null
    private val BRIGHTNESS_OVERLAY = "textures/misc/blind.png".toId()
    private var brightness: Float = 0.5f

    override fun appendCommand(commandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        commandBuilder.apply {
            literal("start") {
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
            literal("settings") {
                argument("value") { float ->
                    runs {
                        brightness = float()
                        this.source.sendMessage("Brightness wurde auf $brightness gesetzt".literal)
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
        //(inGameHud as InGameHudAccessor).invokeRenderOverlay(matrices, BRIGHTNESS, 0.95f)
    }

    @Internal
    fun shaderInjection(program: JsonEffectShaderProgram) {
        program.getUniformByName("Brightness")?.set(brightness)
    }
}

interface SunnyPlayer {
    fun hasStartedLookingAtSun(): Boolean
    fun setHasStartedLookingAtSun(flag: Boolean)
    fun setSunTicks(amount: Int)
    fun getSunTicks(): Int
}
