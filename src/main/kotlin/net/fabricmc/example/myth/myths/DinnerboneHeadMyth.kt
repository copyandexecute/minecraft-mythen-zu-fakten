package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.silkmc.silk.core.text.literal

object DinnerboneHeadMyth : Myth("DinnerboneHead") {
    fun test(livingEntity: LivingEntity): Boolean {
        if (!isActive) return false
        if (livingEntity !is PlayerEntity) return false
        val item = livingEntity.getEquippedStack(EquipmentSlot.HEAD)
        if (!item.isOf(Items.NAME_TAG)) return false
        return item.name.contains("Dinnerbone".literal)
    }
}