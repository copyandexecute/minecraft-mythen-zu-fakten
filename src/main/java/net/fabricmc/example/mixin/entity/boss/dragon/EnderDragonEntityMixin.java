package net.fabricmc.example.mixin.entity.boss.dragon;

import net.fabricmc.example.entity.BoomDragon;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public abstract class EnderDragonEntityMixin extends MobEntity implements BoomDragon {
    private static final TrackedData<Integer> COUNTER = DataTracker.registerData(EnderDragonEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected EnderDragonEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void initDataTrackerInjection(CallbackInfo ci) {
        this.getDataTracker().startTracking(COUNTER, 1);
    }

    @Override
    public void setCounter(int counter) {
        this.dataTracker.set(COUNTER, counter);
    }

    @Override
    public int getCounter() {
        return this.dataTracker.get(COUNTER);
    }
}
