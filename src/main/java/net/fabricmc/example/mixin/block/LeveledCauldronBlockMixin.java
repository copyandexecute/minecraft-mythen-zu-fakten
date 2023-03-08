package net.fabricmc.example.mixin.block;

import net.fabricmc.example.myth.myths.CauldronMlgMyth;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(LeveledCauldronBlock.class)
public abstract class LeveledCauldronBlockMixin extends AbstractCauldronBlock {
    public LeveledCauldronBlockMixin(Settings settings, Map<Item, CauldronBehavior> behaviorMap) {
        super(settings, behaviorMap);
    }

   /* @Inject(method = "onEntityCollision", at = @At("TAIL"))
    private void onEntityCollisionInjection(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        CauldronMlgMyth.INSTANCE.handleMlg(this, world, state, pos, entity);
    } */

    @Override
    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!CauldronMlgMyth.INSTANCE.handleMlg(this, world, state, pos, entity, fallDistance)) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        }
    }
}
