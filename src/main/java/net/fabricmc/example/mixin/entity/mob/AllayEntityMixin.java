package net.fabricmc.example.mixin.entity.mob;

import net.fabricmc.example.entity.InvertedAllay;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AllayEntity.class)
public abstract class AllayEntityMixin extends PathAwareEntity implements InventoryOwner, InvertedAllay {
    private static final TrackedData<Boolean> INVERTED = DataTracker.registerData(AllayEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected AllayEntityMixin(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTrackerInjection(CallbackInfo ci) {
        this.dataTracker.startTracking(INVERTED, false);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbtInjection(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("Inverted", this.isInverted());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbtInjection(NbtCompound nbt, CallbackInfo ci) {
        this.dataTracker.set(INVERTED, nbt.getBoolean("Inverted"));
    }

    @Override
    public void setInverted(boolean inverted) {
        this.dataTracker.set(INVERTED, inverted);
    }

    @Override
    public boolean isInverted() {
        return this.dataTracker.get(INVERTED);
    }
}
