package net.fabricmc.example.mixin.gl;

import net.fabricmc.example.myth.myths.BlindingMyth;
import net.minecraft.client.gl.JsonEffectShaderProgram;
import net.minecraft.client.gl.PostEffectPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PostEffectPass.class)
public abstract class PostEffectPassMixin {
    @Shadow public abstract JsonEffectShaderProgram getProgram();

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gl/JsonEffectShaderProgram;enable()V", shift = At.Shift.BEFORE))
    private void renderInjection(float time, CallbackInfo ci) {
        BlindingMyth.INSTANCE.shaderInjection(this.getProgram());
    }
}
