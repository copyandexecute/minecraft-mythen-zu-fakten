package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.util.math.Direction
import net.silkmc.silk.core.text.literal

object DinnerboneSleepingMyth : Myth("DinnerboneSleeping") {
    fun apply(livingEntity: LivingEntity): Direction {
        return if (!isActive) livingEntity.sleepingDirection!! else livingEntity.sleepingDirection!!.opposite
    }
}