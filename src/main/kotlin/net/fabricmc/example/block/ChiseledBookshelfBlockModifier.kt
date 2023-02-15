package net.fabricmc.example.block

import net.minecraft.block.entity.ChiseledBookshelfBlockEntity
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.EnchantedBookItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtElement
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.silkmc.silk.core.item.itemStack
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

object ChiseledBookshelfBlockModifier {
    fun handleCurseOfBindingBook(
        world: World,
        pos: BlockPos,
        blockEntity: ChiseledBookshelfBlockEntity,
        slot: Int,
        callBack: CallbackInfo,
    ) {
        val itemStack = blockEntity.getStack(slot)
        if (itemStack.isOf(Items.ENCHANTED_BOOK) && itemStack.isCurseOfBindingBook()) {
            world.playSound(null, pos, SoundEvents.ENTITY_VILLAGER_NO, SoundCategory.BLOCKS, 1f, 1f)
            callBack.cancel()
        }
    }

    fun ItemStack.isCurseOfBindingBook(): Boolean {
        return EnchantedBookItem.getEnchantmentNbt(this).any { it.asString().contains("minecraft:binding_curse") }
    }
}
