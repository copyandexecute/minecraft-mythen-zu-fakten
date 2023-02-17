package net.fabricmc.example

import net.fabricmc.example.entity.ClientPlayerEntityModifier.sendCommand
import net.fabricmc.example.entity.FoxEntityModifier.tryGodMlg
import net.fabricmc.example.events.KeyCallback
import net.fabricmc.example.item.GoatHornItemModifier
import net.fabricmc.example.sound.SoundManager
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.entity.EntityType
import net.minecraft.util.math.Box
import net.silkmc.silk.commands.command

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

    SoundManager.init()
    GoatHornItemModifier.init()

    KeyCallback.EVENT.register(KeyCallback { key, action, mods ->
        if (key.code == InputUtil.GLFW_KEY_RIGHT_SHIFT && action == 1) {
            MinecraftClient.getInstance().player?.sendCommand("foxgodmlg")
        }
    })
}
