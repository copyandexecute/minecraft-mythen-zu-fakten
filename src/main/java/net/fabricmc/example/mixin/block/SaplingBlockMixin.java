package net.fabricmc.example.mixin.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SaplingBlock.class)
public abstract class SaplingBlockMixin extends PlantBlock implements Fertilizable, Waterloggable {
    @Shadow @Final public static IntProperty STAGE;
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public SaplingBlockMixin(Settings settings) {
        super(settings);
    }

    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE).add(WATERLOGGED);
    }

    //Ansonsten spawnt wasser direkt mit wegen WATERLOGGED
    public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
        FluidState fluidState = itemPlacementContext.getWorld().getFluidState(itemPlacementContext.getBlockPos());
        boolean bl = fluidState.getFluid() == Fluids.WATER;
        return (BlockState)((BlockState)super.getPlacementState(itemPlacementContext).with(WATERLOGGED, bl));
    }

    //Das ansonsten rendert wasser nicht in block
    public FluidState getFluidState(BlockState blockState) {
        return (Boolean)blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }
}
