package net.fabricmc.example.mixin.entity.projectile;

import net.fabricmc.example.myth.myths.FireworkRocketEntityMyth;
import net.fabricmc.example.myth.myths.FireworkRocketMyth;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity implements FlyingItemEntity, FireworkRocketEntityMyth {
    @Shadow
    @Final
    private static TrackedData<OptionalInt> SHOOTER_ENTITY_ID;

    @Shadow
    private @Nullable LivingEntity shooter;

    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void setVelocityInjection(LivingEntity instance, Vec3d vec3d) {
        FireworkRocketMyth.INSTANCE.setFireworkVelocity(instance, vec3d);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;setPosition(DDD)V"))
    private void setPositionInjection(FireworkRocketEntity instance, double x, double y, double z) {
        FireworkRocketMyth.INSTANCE.positionInjection(instance, x, y, z, shooter);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FireworkRocketEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V", ordinal = 0))
    private void setVelocityInjection(FireworkRocketEntity instance, Vec3d vec3d) {
        FireworkRocketMyth.INSTANCE.velocityInjection(instance, vec3d, shooter);
    }

    @Override
    public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
        return super.shouldRender(cameraX, cameraY, cameraZ);  //TODO Lazy
    }

    public boolean shouldRender(double distance) {
        return distance < 4096.0; //TODO Lazy
    }

    @Override
    public void setShooter(LivingEntity livingEntity) {
        this.dataTracker.set(SHOOTER_ENTITY_ID, OptionalInt.of(livingEntity.getId()));
        this.shooter = livingEntity;
    }
}
