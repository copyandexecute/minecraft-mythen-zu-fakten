package net.fabricmc.example.mixin.entity.player;

import net.fabricmc.example.entity.LeashPlayer;
import net.fabricmc.example.entity.RocketSpammer;
import net.fabricmc.example.mixin.world.WorldAccessor;
import net.fabricmc.example.myth.myths.SunnyPlayer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements LeashPlayer, RocketSpammer, SunnyPlayer {
    @Shadow
    public abstract ItemStack eatFood(World world, ItemStack stack);

    @Shadow
    protected abstract Vec3d adjustMovementForSneaking(Vec3d movement, MovementType type);

    private static final TrackedData<Float> SUN_BRIGHTNESS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private final Set<UUID> entities = new HashSet<>();
    private int usedRockets;
    private long lastUsedRocketTimeStamp;
    private boolean hasStartedLookingAtSun;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovementInjection(CallbackInfo ci) {
        Vec3d vec3d = this.getVelocity();
        int attachedChickens = getAttachedChickens();
        if (attachedChickens > 0 && !this.onGround && vec3d.y < 0.0) {
            this.setVelocity(vec3d.multiply(1.0, 1.0 / attachedChickens, 1.0));
        }
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTrackerInjection(CallbackInfo ci) {
        this.dataTracker.startTracking(SUN_BRIGHTNESS, 0.0f);
    }

    @NotNull
    @Override
    public Set<UUID> getEntities() {
        return entities;
    }

    private int getAttachedChickens() {
        return (int) entities.stream().map(uuid -> ((WorldAccessor) world).invokeGetEntityLookup().get(uuid)).filter(Objects::nonNull).count();
    }

    @Override
    public void handleUuid(@NotNull UUID uuid, boolean attached) {
        if (attached) {
            entities.add(uuid);
        } else {
            entities.remove(uuid);
        }
    }

    @Override
    public void addRocket() {
        usedRockets++;
        lastUsedRocketTimeStamp = System.currentTimeMillis();
    }

    @Override
    public int getUsedRockets() {
        return usedRockets;
    }

    @Override
    public long getLastUsedRocket() {
        return lastUsedRocketTimeStamp;
    }

    @Override
    public void resetRockets() {
        usedRockets = 0;
        lastUsedRocketTimeStamp = 0;
    }

    @Override
    public boolean hasStartedLookingAtSun() {
        return hasStartedLookingAtSun;
    }

    @Override
    public void setHasStartedLookingAtSun(boolean flag) {
        this.hasStartedLookingAtSun = flag;
    }

    @Override
    public void setSunBlindness(float amount) {
        this.dataTracker.set(SUN_BRIGHTNESS, amount);
    }

    @Override
    public float getSunBlindness() {
        return this.dataTracker.get(SUN_BRIGHTNESS);
    }
}
