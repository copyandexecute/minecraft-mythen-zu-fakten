package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.item.ItemStack

object SpongeMyth : Myth("Sponge") {
    fun BetterSponge.apply(original: Int): Int {
        val modifier = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, fromItemStack)
        return original + (modifier * (original / 2))
    }
}

interface BetterSponge {
    val fromItemStack: ItemStack
}