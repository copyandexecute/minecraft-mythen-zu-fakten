package net.fabricmc.example

import net.fabricmc.example.command.CommandManager
import net.fabricmc.example.entity.ClientPlayerEntityModifier.sendCommand
import net.fabricmc.example.entity.FoxEntityModifier.tryGodMlg
import net.fabricmc.example.entity.LeashPlayerModifier
import net.fabricmc.example.events.KeyCallback
import net.fabricmc.example.extensions.toMcId
import net.fabricmc.example.item.GoatHornItemModifier
import net.fabricmc.example.mixin.accessor.GameRendererAccessor
import net.fabricmc.example.render.entity.EnderDragonEntityRendererModifier
import net.fabricmc.example.sound.SoundManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.EntityType
import net.minecraft.text.Text
import net.minecraft.util.math.Box
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.command
import net.silkmc.silk.commands.sendSuccess
import net.silkmc.silk.core.text.literal

fun init() {
    command("foxgodmlg") {
        runs {
            for (it in this.source.world.getEntitiesByType(
                EntityType.FOX,
                Box.of(this.source.position, 15.0, 15.0, 15.0)
            ) { true }) {
                it.tryGodMlg()
            }
        }
    }


    clientCommand("shader") {
        argument<String>("name") { shaderName ->
            runs {
                (MinecraftClient.getInstance().gameRenderer as GameRendererAccessor).apply {
                    invokeLoadPostProcessor("shaders/post/${shaderName()}.json".toMcId())
                }
                this.source.sendSuccess("Loaded ${shaderName()}".literal)
            }
        }
    }

    command("dragonreset") {
        runs {
            this.source.sendMessage(Text.of("Dragon Light set from ${EnderDragonEntityRendererModifier.killedDragons} to 0"))
            EnderDragonEntityRendererModifier.killedDragons = 0
        }
    }

    SoundManager.init()
    GoatHornItemModifier.init()
    LeashPlayerModifier.init()
    CommandManager.init()

    KeyCallback.EVENT.register(KeyCallback { key, action, mods ->
        if (key.code == InputUtil.GLFW_KEY_RIGHT_SHIFT && action == 1) {
            MinecraftClient.getInstance().player?.sendCommand("foxgodmlg")
        }
    })
}
