package net.fabricmc.example.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SugarCaneBlock.class)
public abstract class SugarCaneBlockMixin extends Block {
    @Shadow
    @Final
    public static IntProperty AGE;

    public SugarCaneBlockMixin(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.isAir(pos.up())) {
            int i = 1;
            while (world.getBlockState(pos.down(i)).isOf(this)) ++i;

            BlockState blockState = world.getBlockState(pos.down(i));
            boolean isRedSand = blockState.isOf(Blocks.RED_SAND);

            if (i < 50) {
                int j = state.get(AGE);
                if (j == 15) {
                    world.setBlockState(pos.up(), this.getDefaultState());
                    world.setBlockState(pos, state.with(AGE, 0), Block.NO_REDRAW);
                } else {
                    world.setBlockState(pos, state.with(AGE, Math.min(15, !isRedSand ? j + 1 : j + random.nextBetween(1, 6))), Block.NO_REDRAW);
                }
            }
        }

    }
}
