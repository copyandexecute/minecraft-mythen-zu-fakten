package net.fabricmc.example.mixin.entity.mob;

import net.fabricmc.example.myth.myths.EndermanArrowMyth;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin extends HostileEntity implements Angerable {
    protected EndermanEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void arrowMyth(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (EndermanArrowMyth.INSTANCE.isActive() && source.getSource() instanceof ProjectileEntity) {
            cir.setReturnValue(super.damage(source, amount));
        }
    }
}
