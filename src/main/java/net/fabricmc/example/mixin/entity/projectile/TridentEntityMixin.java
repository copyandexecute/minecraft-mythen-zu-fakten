package net.fabricmc.example.mixin.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    @Shadow
    protected abstract ItemStack asItemStack();

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    private int piercingLevel;

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void constructorInjection(World world, LivingEntity owner, ItemStack stack, CallbackInfo ci) {
        for (NbtElement enchantment : stack.getEnchantments()) {
            if (enchantment.toString().contains("piercing")) {
                this.piercingLevel = ((NbtCompound) enchantment).getInt("lvl");
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickInjection(CallbackInfo ci) {
        if (piercingLevel == 0) return;
        for (BlockPos block : getBlocks(this.getBlockPos(), piercingLevel)) {
            world.breakBlock(block, true);
        }
    }

    public List<BlockPos> getBlocks(BlockPos start, int radius) {
        List<BlockPos> blocks = new ArrayList<>();
        for (double x = start.getX() - radius; x <= start.getX() + radius; x++) {
            for (double y = start.getY() - radius; y <= start.getY() + radius; y++) {
                for (double z = start.getZ() - radius; z <= start.getZ() + radius; z++) {
                    blocks.add(new BlockPos((int) x, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        if (this.piercingLevel == 0) {
            super.onBlockHit(blockHitResult);
        }
    }
}
