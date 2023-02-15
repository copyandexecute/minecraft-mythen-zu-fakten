package net.fabricmc.example.block

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object ChiseledBookshelfBlockModifier {
    fun handleCurseOfBindingBook(playerEntity: PlayerEntity, blockEntity: ChiseledBookshelfBlockEntity, slot: Int, callBack: CallbackInfo) {
        val itemStack = blockEntity.getStack(slot)
        if (itemStack.isOf(Items.ENCHANTED_BOOK) && EnchantmentHelper.hasBindingCurse(itemStack)) {
            playerEntity.playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE,0.5f,0.3f)
            callBack.cancel()
        }
    }
}