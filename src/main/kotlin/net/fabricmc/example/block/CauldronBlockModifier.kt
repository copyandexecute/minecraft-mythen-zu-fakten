package net.fabricmc.example.block

import net.minecraft.entity.Entity
import net.minecraft.text.Text
import net.silkmc.silk.commands.command
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask

object CauldronBlockModifier {
    var isActive = true
    fun init() {
        command("togglecauldronmlg") {
            runs {
                isActive = !isActive
                this.source.sendMessage(Text.of("Cauldronmlg $isActive"))
            }
        }
    }
    fun handleMlg(entity: Entity) {
        entity.isInvulnerable = true
        mcCoroutineTask(delay = 5.ticks) {
            entity.isInvulnerable = false
        }
    }
}