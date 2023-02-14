package net.fabricmc.example.entity

import net.minecraft.block.Blocks
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.silkmc.silk.core.kotlin.ticks
import net.silkmc.silk.core.task.mcCoroutineTask

object LivingEntityModifier {
    fun LivingEntity.tryMlg(landedPosition: BlockPos): Boolean {
       // if (this.getEquippedStack(EquipmentSlot.MAINHAND).isOf(Items.WATER_BUCKET)) {
        if (true) {
            playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1f, 1f)
            equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.BUCKET))
            world.setBlockState(landedPosition.up(1), Blocks.WATER.defaultState)
            mcCoroutineTask(delay = 5.ticks) {
                world.setBlockState(landedPosition.up(1), Blocks.AIR.defaultState)
                equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.WATER_BUCKET))
                playSound(SoundEvents.ITEM_BUCKET_FILL, 1f, 1f)
            }
            return true
        }
        return false
    }
}
