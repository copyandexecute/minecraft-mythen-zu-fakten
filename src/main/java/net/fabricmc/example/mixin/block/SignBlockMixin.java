package net.fabricmc.example.mixin.block;

import net.fabricmc.example.myth.myths.JebSignMyth;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SignBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SignType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SignBlock.class)
public class SignBlockMixin extends AbstractSignBlock {
    protected SignBlockMixin(Settings settings, SignType type) {
        super(settings, type);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult actionResult = JebSignMyth.INSTANCE.onUse(world, pos, player, hand);
        return actionResult == ActionResult.FAIL ? super.onUse(state, world, pos, player, hand, hit) : actionResult;
    }
}
