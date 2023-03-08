package net.fabricmc.example.mixin.accessor;

import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractCauldronBlock.class)
public interface AbstractCauldronBlockAccessor {
    @Invoker("isEntityTouchingFluid")
    boolean invokeIsEntityTouchingFluid(BlockState state, BlockPos pos, Entity entity);
}
