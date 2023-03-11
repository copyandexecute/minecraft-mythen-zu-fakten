package net.fabricmc.example.myth.myths

import kotlinx.coroutines.Job
import net.fabricmc.example.extensions.toId
import net.fabricmc.example.extensions.toMcId
import net.fabricmc.example.mixin.accessor.GameRendererAccessor
import net.fabricmc.example.mixin.accessor.InGameHudAccessor
import net.fabricmc.example.myth.Myth
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.JsonEffectShaderProgram
import net.minecraft.client.gui.hud.InGameHud
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.hit.HitResult
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.task.infiniteMcCoroutineTask
import net.silkmc.silk.core.text.literal
import org.jetbrains.annotations.ApiStatus.Internal
import kotlin.math.abs

object BlindingMyth : Myth("Blinding", true) {
    private enum class Type {
        SHADER, OVERLAY
    }

    private val START = Pair(0, -10f)
    private val ZENIT = Pair(6000, -90f)
    private val STEIGUNG = (ZENIT.second - START.second) / (ZENIT.first - START.first)
    private var job: Job? = null
    private val BRIGHTNESS_OVERLAY = "textures/misc/blind.png".toId()
    private val BLINDING_SHADER = "shaders/post/blinding.json".toMcId()
    private var type = Type.OVERLAY

    override fun appendCommand(commandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        commandBuilder.apply {
            literal("start") {
                runs {
                    val server = this.source.server
                    job?.cancel()
                    job = infiniteMcCoroutineTask {
                        server.playerManager.playerList.forEach {
                            val sunnyPlayer = it as SunnyPlayer
                            if (it.isLookingAtSun) {
                                sunnyPlayer.setSunBlindness(0.99f.coerceAtMost(sunnyPlayer.getSunBlindness() + 0.09F))
                            } else {
                                when (type) {
                                    Type.SHADER -> sunnyPlayer.setSunBlindness(0.5f.coerceAtLeast(sunnyPlayer.getSunBlindness() - 0.01f))
                                    Type.OVERLAY -> sunnyPlayer.setSunBlindness(0.0f.coerceAtLeast(sunnyPlayer.getSunBlindness() - 0.01f))
                                }
                            }
                        }
                    }
                }
            }
            literal("mode") {
                argument<String>("mode") { mode ->
                    suggestList { Type.values().map { it.name } }
                    runs {
                        type = Type.valueOf(mode().uppercase())
                        this.source.sendMessage("Modus: $type".literal)
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
        if (isActive && type == Type.OVERLAY) {
            val sunnyPlayer = MinecraftClient.getInstance().player as? SunnyPlayer? ?: return
            (inGameHud as InGameHudAccessor).invokeRenderOverlay(
                matrices,
                BRIGHTNESS_OVERLAY,
                sunnyPlayer.getSunBlindness()
            )
        }
    }

    @Internal
    fun shaderInjection(program: JsonEffectShaderProgram) {
        val sunnyPlayer = MinecraftClient.getInstance().player as? SunnyPlayer? ?: return
        program.getUniformByName("Brightness")?.set(sunnyPlayer.getSunBlindness())
    }

    fun onCameraEntitySet(entity: Entity?) {
        if (isActive && MinecraftClient.getInstance().gameRenderer.postProcessor?.name != BLINDING_SHADER.toString() && type == Type.SHADER) {
            (MinecraftClient.getInstance().gameRenderer as GameRendererAccessor).invokeLoadPostProcessor(BLINDING_SHADER)
        }
    }
}

interface SunnyPlayer {
    fun hasStartedLookingAtSun(): Boolean
    fun setHasStartedLookingAtSun(flag: Boolean)
    fun setSunBlindness(amount: Float)
    fun getSunBlindness(): Float
}
