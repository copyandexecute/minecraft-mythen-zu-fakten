package net.fabricmc.example.myth.myths

import net.fabricmc.example.mixin.accessor.LeafEntryAccessor
import net.fabricmc.example.myth.Myth
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.provider.number.LootNumberProvider
import net.minecraft.util.math.MathHelper

object LuckMyth : Myth("Luck") {
    fun LootNumberProvider.apply(lootContext: LootContext): Float {
        return if (isActive) 1f else nextFloat(lootContext)
    }

    fun LivingEntity.apply(): Int {
        return if (!isActive or (this !is PlayerEntity)) EnchantmentHelper.getLooting(this) else (this as PlayerEntity).luck.toInt()
    }

    fun LeafEntryAccessor.apply(luck: Float): Int {
        val luckyAndQuality: Float = if (weight <= 10 && isActive) {
            weight * luck
        } else {
            quality * luck
        }
        return MathHelper.floor(weight + luckyAndQuality).coerceAtLeast(0)
    }
}
