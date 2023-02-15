package net.fabricmc.example.mixin.block;

import net.fabricmc.example.block.ChiseledBookshelfBlockModifier;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlock.class)
public abstract class ChiseledBookshelfBlockMixin {
    @Inject(method = "tryRemoveBook", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/ChiseledBookshelfBlockEntity;removeStack(II)Lnet/minecraft/item/ItemStack;"), cancellable = true)
    private static void tryRemoveBookInjection(World world, BlockPos pos, PlayerEntity player, ChiseledBookshelfBlockEntity blockEntity, int slot, CallbackInfo ci) {
        ChiseledBookshelfBlockModifier.INSTANCE.handleCurseOfBindingBook(blockEntity,slot,ci);
    }
}
