package net.fabricmc.example.myth.myths

import net.fabricmc.example.mixin.accessor.AbstractCauldronBlockAccessor
import net.fabricmc.example.myth.Myth
import net.minecraft.block.AbstractCauldronBlock
import net.minecraft.block.BlockState
import net.minecraft.entity.Entity
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object CauldronMlgMyth : Myth("CauldronMLG", true) {
    fun AbstractCauldronBlock.handleMlg(
        world: World,
        state: BlockState,
        pos: BlockPos,
        entity: Entity,
        fallDistance: Float
    ): Boolean {
        if (!world.isClient && (this as AbstractCauldronBlockAccessor).invokeIsEntityTouchingFluid(
                state,
                pos,
                entity
            ) && isActive && fallDistance > 0.0
        ) {
            world.playSound(null, entity.blockPos, SoundEvents.ENTITY_PLAYER_SPLASH, SoundCategory.PLAYERS, 1f, 1f)
            return true
        }
        return false
    }
}
