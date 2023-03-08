package net.fabricmc.example.mixin.block;

import net.fabricmc.example.myth.myths.BetterSponge;
import net.fabricmc.example.myth.myths.SpongeMyth;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SpongeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SpongeBlock.class)
public abstract class SpongeBlockMixin extends Block implements BetterSponge {
    private ItemStack itemStack;

    public SpongeBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        this.itemStack = itemStack;
    }

    @NotNull
    @Override
    public ItemStack getFromItemStack() {
        return itemStack;
    }

    @ModifyConstant(method = "absorbWater", constant = @Constant(intValue = 64))
    private int spongeMyth(int value) {
        return (SpongeMyth.INSTANCE.isActive()) ? 10000 : value;
    }

    @ModifyConstant(method = "absorbWater", constant = @Constant(intValue = 6))
    private int spongeMyth2(int value) {
        return SpongeMyth.INSTANCE.apply(this, value);
    }
}
