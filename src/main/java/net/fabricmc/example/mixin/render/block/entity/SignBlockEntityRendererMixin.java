package net.fabricmc.example.mixin.render.block.entity;

import net.fabricmc.example.myth.myths.JebSignMyth;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SignBlockEntityRenderer.class)
public abstract class SignBlockEntityRendererMixin implements BlockEntityRenderer<SignBlockEntity> {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private static void jebMyth(SignBlockEntity sign, CallbackInfoReturnable<Integer> cir) {
        JebSignMyth.INSTANCE.apply(sign, cir);
    }

    @Redirect(method = "renderText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Lnet/minecraft/text/OrderedText;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/font/TextRenderer$TextLayerType;II)I"))
    private int jebMyth(TextRenderer instance, OrderedText text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextRenderer.TextLayerType layerType, int backgroundColor, int light, SignBlockEntity signBlockEntity) {
        return JebSignMyth.INSTANCE.apply(signBlockEntity, instance, text, x, y, color, shadow, matrix, vertexConsumers, layerType, backgroundColor, light);
    }
}
