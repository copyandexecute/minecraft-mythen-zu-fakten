package net.fabricmc.example.mixin.block;

import net.fabricmc.example.block.MudBlockModifier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MudBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MudBlock.class)
public abstract class MudBlockMixin extends Block {

    public MudBlockMixin(Settings settings) {
        super(settings);
    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        MudBlockModifier.INSTANCE.handleFire(entity, world, state, pos);
    }
}
