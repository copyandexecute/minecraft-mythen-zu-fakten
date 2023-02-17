package net.fabricmc.example.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.event.GameEvent
import kotlin.random.Random

object MudBlockModifier {
    fun handleFire(entity: Entity, world: World, state: BlockState, pos: BlockPos) {
        if (entity is LivingEntity && entity.isOnFire()) {
            entity.extinguish()
            entity.playSound(
                SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                1f,
                1.6f + (Random.nextFloat() - Random.nextFloat()) * 0.4f
            )
            entity.setOnFire(false)
            //das funny kannst im video zeigen
            val blockState = Block.pushEntitiesUpBeforeBlockChange(state, Blocks.DIRT.defaultState, world, pos)
            world.setBlockState(pos, blockState)
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState))
        }
    }
}