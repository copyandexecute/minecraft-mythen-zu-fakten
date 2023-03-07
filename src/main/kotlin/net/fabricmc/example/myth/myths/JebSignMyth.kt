package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.server.command.RaidCommand
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.CharacterVisitor
import net.minecraft.text.OrderedText
import net.minecraft.text.Style
import net.minecraft.util.ActionResult
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.text.literal
import org.joml.Matrix4f
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import java.awt.Color


object JebSignMyth : Myth("JebSign") {
    private var mode = Mode.STATIC

    private enum class Mode {
        STATIC, WAVE
    }

    private fun rainbowEffect(): Int {
        return Color.HSBtoRGB((System.currentTimeMillis() % 7000L).toFloat() / 7000f, 1f, 1f)
    }

    private fun offsetRainbowEffect(x: Long, y: Long): Int {
        val longHue = System.currentTimeMillis() - (x * 10 - y * 10)
        return Color.HSBtoRGB(longHue % 3000 / 3000.0f, 1f, 1f)
    }

    override fun appendCommand(commandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        commandBuilder.argument<String>("mode") { mode ->
            suggestList { Mode.values().map { it.name } }
            runs {
                this@JebSignMyth.mode = Mode.valueOf(mode().uppercase())
            }
        }
    }

    fun onUse(
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand
    ): ActionResult {
        val itemStack = player.getStackInHand(hand)
        if (!isActive) return ActionResult.FAIL
        if (world.isClient) return ActionResult.FAIL
        if (!itemStack.isOf(Items.NAME_TAG)) return ActionResult.FAIL
        if (!player.abilities.allowModifyWorld) return ActionResult.FAIL
        if (!itemStack.name.contains("jeb_".literal)) return ActionResult.FAIL
        val blockEntity = world.getBlockEntity(pos) as? SignBlockEntity ?: return ActionResult.FAIL

        world.playSound(
            null,
            pos,
            SoundEvents.ITEM_DYE_USE,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f
        )
        (blockEntity as IRainbow).setRainbow(true)

        if (!player.isCreative) {
            itemStack.decrement(1)
        }

        return if (blockEntity.onActivate(player as ServerPlayerEntity)) ActionResult.SUCCESS else ActionResult.PASS
    }

    fun apply(sign: SignBlockEntity, cir: CallbackInfoReturnable<Int>) {
        if (sign.isRainbow && isActive) {
            cir.returnValue = rainbowEffect()
        }
    }

    private val SignBlockEntity.isRainbow: Boolean get() = (this as? IRainbow?)?.isRainbow() == true

    fun apply(
        blockEntity: SignBlockEntity,
        instance: TextRenderer,
        text: OrderedText,
        x: Float,
        y: Float,
        color: Int,
        shadow: Boolean,
        matrix: Matrix4f,
        vertexConsumers: VertexConsumerProvider,
        seeThrough: Boolean,
        backgroundColor: Int,
        light: Int
    ): Int {
        if (!isActive or !blockEntity.isRainbow) {
            instance.draw(
                text,
                x,
                y,
                color,
                shadow,
                matrix,
                vertexConsumers,
                seeThrough,
                backgroundColor,
                light
            )
            return -1
        }

        when (mode) {
            Mode.STATIC -> {
                instance.draw(
                    text,
                    x,
                    y,
                    color,
                    shadow,
                    matrix,
                    vertexConsumers,
                    seeThrough,
                    backgroundColor,
                    light
                )
            }

            Mode.WAVE -> {
                StringVisitor().apply {
                    text.accept(this)
                }.text { string ->
                    var currentX = x
                    Formatting.strip(string)?.toCharArray()?.map { it.toString().literal }?.forEach {
                        instance.draw(
                            it,
                            currentX,
                            y,
                            offsetRainbowEffect(currentX.toLong(), y.toLong()),
                            shadow,
                            matrix,
                            vertexConsumers,
                            seeThrough,
                            backgroundColor,
                            light
                        )
                        currentX += instance.getWidth(it)
                    }
                }
            }
        }
        return -1 //don't know what this is for
    }

    //Thanks to https://github.com/Wurst-Imperium/Wurst7/blob/master/src/main/java/net/wurstclient/hacks/AntiSpamHack.java
    internal class StringVisitor : CharacterVisitor {
        private var sb = StringBuilder()
        override fun accept(index: Int, style: Style, codePoint: Int): Boolean {
            sb.appendCodePoint(codePoint)
            return true
        }

        inline fun text(string: (String) -> Unit) {
            string.invoke(toString())
        }

        override fun toString(): String = sb.toString()
    }
}

interface IRainbow {
    fun setRainbow(flag: Boolean)
    fun isRainbow(): Boolean
}
