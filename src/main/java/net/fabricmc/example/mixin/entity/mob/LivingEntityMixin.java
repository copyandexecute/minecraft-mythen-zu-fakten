package net.fabricmc.example.mixin.entity.mob;

import net.fabricmc.example.entity.LivingEntityModifier;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow
    public abstract void equipStack(EquipmentSlot slot, ItemStack stack);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "fall", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(DD)D"), cancellable = true)
    private void fallHitGroundInjection(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition, CallbackInfo ci) {
        if (this.getType() == EntityType.FOX) {
            if (LivingEntityModifier.INSTANCE.tryMlg((LivingEntity) ((Object) this), landedPosition)) {
                ci.cancel();
            }
        }
    }
}
