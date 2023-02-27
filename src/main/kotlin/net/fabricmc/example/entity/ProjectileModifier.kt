package net.fabricmc.example.entity

import net.minecraft.text.Text
import net.silkmc.silk.commands.command

object ProjectileModifier {
    var isActive = true
    fun init() {
        command("toggleendermanprojectile") {
            runs {
                isActive = !isActive
                this.source.sendMessage(Text.of("Projectile $isActive"))
            }
        }
    }
}