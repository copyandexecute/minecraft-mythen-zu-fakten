package net.fabricmc.example.mixin.item;

import net.fabricmc.example.myth.myths.FireworkRocketMyth;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends RangedWeaponItem implements Vanishable {
    @Shadow
    private static PersistentProjectileEntity createArrow(World world, LivingEntity entity, ItemStack crossbow, ItemStack arrow) {
        return null;
    }

    public CrossbowItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * @author NoRiskk
     * @reason sorry habs nicht hinbekommen mit mixins da waren nur errors und f0x hat nicht geantwortet
     */
    @Overwrite
    private static void shoot(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
        if (!world.isClient) {
            boolean bl = projectile.isOf(Items.FIREWORK_ROCKET);
            Object projectileEntity;
            if (bl) {
                projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15000000596046448, shooter.getZ(), true);
                FireworkRocketMyth.INSTANCE.apply(((FireworkRocketEntity) projectileEntity), shooter);
            } else {
                projectileEntity = createArrow(world, shooter, crossbow, projectile);
                if (creative || simulated != 0.0F) {
                    ((PersistentProjectileEntity) projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof CrossbowUser) {
                CrossbowUser crossbowUser = (CrossbowUser) shooter;
                crossbowUser.shoot(crossbowUser.getTarget(), crossbow, (ProjectileEntity) projectileEntity, simulated);
            } else {
                Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double) (simulated * 0.017453292F), vec3d.x, vec3d.y, vec3d.z);
                Vec3d vec3d2 = shooter.getRotationVec(1.0F);
                Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
                ((ProjectileEntity) projectileEntity).setVelocity((double) vector3f.x(), (double) vector3f.y(), (double) vector3f.z(), speed, divergence);
            }

            crossbow.damage(bl ? 3 : 1, shooter, (e) -> {
                e.sendToolBreakStatus(hand);
            });
            world.spawnEntity((Entity) projectileEntity);
            world.playSound((PlayerEntity) null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
        }
    }
}
