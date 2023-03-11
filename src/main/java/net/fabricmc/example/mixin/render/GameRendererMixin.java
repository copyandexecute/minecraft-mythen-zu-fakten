package net.fabricmc.example.mixin.render;

import net.fabricmc.example.myth.myths.BlindingMyth;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Inject(method = "onCameraEntitySet", at = @At("TAIL"))
    private void onCameraEntitySetInjection(Entity entity, CallbackInfo ci) {
        BlindingMyth.INSTANCE.onCameraEntitySet(entity);
    }
}
