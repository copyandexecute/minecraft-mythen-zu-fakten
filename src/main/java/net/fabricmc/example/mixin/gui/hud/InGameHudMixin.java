package net.fabricmc.example.mixin.gui.hud;

import net.fabricmc.example.myth.myths.BlindingMyth;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F"))
    private void blindingMyth(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        BlindingMyth.INSTANCE.renderOverlay((InGameHud) (Object) this, matrices);
    }
}
