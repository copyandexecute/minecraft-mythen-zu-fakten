package net.fabricmc.example.myth.myths

import net.fabricmc.example.myth.Myth
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.provider.number.LootNumberProvider

object LuckMyth : Myth("Luck") {
    fun LootNumberProvider.apply(lootContext: LootContext): Float {
        return if (isActive) 1f else nextFloat(lootContext)
    }
}