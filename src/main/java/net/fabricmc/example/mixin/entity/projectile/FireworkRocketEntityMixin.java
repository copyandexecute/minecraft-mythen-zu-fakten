package net.fabricmc.example.mixin.entity.projectile;

import net.fabricmc.example.entity.RocketSpammer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityMixin extends ProjectileEntity implements FlyingItemEntity {
    @Shadow
    private @Nullable LivingEntity shooter;

    public FireworkRocketEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private void setVelocityInjection(LivingEntity instance, Vec3d vec3d) {
        if (this.shooter instanceof RocketSpammer spammer) {
            double d = 1.5;
            double e = 0.1 * Math.max(1, spammer.getUsedRockets());
            //TODO Needs speed adjustment
            Vec3d vec3d2 = this.shooter.getVelocity();
            System.out.println("Rockets: " + spammer.getUsedRockets());
            this.shooter.setVelocity(vec3d2.add(
                    vec3d.x * e + (vec3d.x * d - vec3d2.x) * 0.5,
                    vec3d.y * e + (vec3d.y * d - vec3d2.y) * 0.5,
                    vec3d.z * e + (vec3d.z * d - vec3d2.z) * 0.5
            ));
        }
    }
}
