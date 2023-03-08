package net.fabricmc.example.mixin.entity.projectile;

import net.fabricmc.example.myth.myths.EndermanArrowMyth;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyVariable(method = "onEntityHit", at = @At("STORE"), ordinal = 0)
    private boolean endermanInjection(boolean value) {
        return !EndermanArrowMyth.INSTANCE.isActive() && value;
    }
}
