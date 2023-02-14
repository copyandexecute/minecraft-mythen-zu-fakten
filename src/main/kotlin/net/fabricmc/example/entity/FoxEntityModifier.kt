package net.fabricmc.example.entity

import net.fabricmc.example.entity.FoxEntityModifier.tryGodMlg
import net.fabricmc.example.mixin.entity.mob.FoxEntityInvoker
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.passive.FoxEntity
import net.minecraft.item.Items
import net.silkmc.silk.core.entity.directionVector
import net.silkmc.silk.core.entity.modifyVelocity
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask

object FoxEntityModifier {
    fun FoxEntity.tryGodMlg() {
        val equippedStack = this.getEquippedStack(EquipmentSlot.MAINHAND)
        if (equippedStack.isOf(Items.WATER_BUCKET)) {
            this.setCrouching(false)
            (this as FoxEntityInvoker).invokeSpit(equippedStack)
            this.equipStack(EquipmentSlot.MAINHAND, Items.AIR.defaultStack)
            mcCoroutineTask(delay = 10.ticks) {
                this@tryGodMlg.modifyVelocity(directionVector.x * 0.3, 2, directionVector.z * 0.3)
            }
        }
    }
}
