package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.fabricmc.example.myth.myths.DinnerboneSleepingMyth.flip
import net.minecraft.entity.EntityPose
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.util.math.Direction
import net.silkmc.silk.core.text.literal

object DinnerboneSleepingMyth : Myth("DinnerboneSleeping") {
    fun apply(livingEntity: LivingEntity): Direction {
        return if (!livingEntity.flip) livingEntity.sleepingDirection!! else livingEntity.sleepingDirection!!.opposite
    }

    fun translateSleeping(instance: LivingEntity, entityPose: EntityPose): Float {
        return if (!instance.flip) instance.getEyeHeight(entityPose) else (instance.getEyeHeight(entityPose) / 2) - 0.3f
    }

    val LivingEntity.flip
        get() = isActive && (customName?.contains("Dinnerbone".literal) == true || customName?.contains("Grum".literal) == true)
}
