package net.fabricmc.example.mixin.render.entity;

import net.fabricmc.example.myth.myths.DinnerboneMyth;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Inject(method = "shouldFlipUpsideDown", at = @At("HEAD"), cancellable = true)
    private static void dinnerboneMyth(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (DinnerboneMyth.INSTANCE.test(entity)) cir.setReturnValue(true);
    }
}
