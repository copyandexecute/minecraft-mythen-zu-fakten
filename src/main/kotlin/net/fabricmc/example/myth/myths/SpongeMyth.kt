package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack

object SpongeMyth : Myth("Sponge") {
    fun BetterSponge.apply(original: Int): Int {
        val modifier = if (isActive) EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, fromItemStack) else 0
        return original + (modifier * (original))
    }
}

interface BetterSponge {
    val fromItemStack: ItemStack
}
