package net.fabricmc.example.mixin.entity.boss.dragon.phase;

import net.fabricmc.example.entity.BoomDragon;
import net.fabricmc.example.render.entity.EnderDragonEntityRendererModifier;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.phase.AbstractPhase;
import net.minecraft.entity.boss.dragon.phase.DyingPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DyingPhase.class)
public abstract class DyingPhaseMixin extends AbstractPhase {
    public DyingPhaseMixin(EnderDragonEntity dragon) {
        super(dragon);
    }

    @Inject(method = "beginPhase", at = @At("TAIL"))
    private void beginPhaseInjection(CallbackInfo ci) {
        EnderDragonEntityRendererModifier.INSTANCE.increaseKilledDragons(1);
        ((BoomDragon) dragon).setCounter(EnderDragonEntityRendererModifier.INSTANCE.getKilledDragons());
    }
}
